package infrastructure.persistance;

import core.entities.Account;
import core.entities.Transaction;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DataMappingException;
import core.exceptions.DatabaseOperationException;
import core.exceptions.InvalidPinCodeException;
import core.repositories.AccountRepository;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import core.valueobjects.TransactionType;
import infrastructure.configs.AppConfig;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryImpl implements AccountRepository {
    private final String connectionString;

    public AccountRepositoryImpl(String connectionString) {
        this.connectionString = connectionString;
    }

    public AccountRepositoryImpl() {
        this.connectionString = AppConfig.getConnectionString();
    }

    @Override
    public Account getAccountByNumber(String accountNumber) throws SQLException {
        String query = "SELECT * FROM accounts WHERE accountnumber = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    UUID id = UUID.fromString(resultSet.getString("id"));
                    String pinCode = resultSet.getString("pincode");
                    BigDecimal balanceAmount = resultSet.getBigDecimal("balance");
                    CurrencyType currency = CurrencyType.valueOf(resultSet.getString("currency"));

                    Money balance = new Money(balanceAmount, currency);
                    List<Transaction> transactions = loadTransactions(id, connection);

                    return new Account(id, accountNumber, pinCode, balance, transactions);
                }
            }
        }
        return null;
    }

    @Override
    public List<Transaction> loadTransactions(UUID accountId, Connection connection) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String transactionQuery = "SELECT * FROM transactions WHERE accountid = ?";

        if (connection == null) {
            throw new SQLException("Database connection is null.");
        }

        try (PreparedStatement transactionStatement = connection.prepareStatement(transactionQuery)) {
            transactionStatement.setObject(1, accountId);
            try (ResultSet transactionResultSet = transactionStatement.executeQuery()) {
                while (transactionResultSet.next()) {
                    UUID transactionId = UUID.fromString(transactionResultSet.getString("id"));
                    BigDecimal amount = transactionResultSet.getBigDecimal("amount");
                    CurrencyType transactionCurrency = CurrencyType.valueOf(transactionResultSet.getString("currency"));
                    TransactionType typeValue = TransactionType.valueOf(transactionResultSet.getString("type"));
                    Instant date = transactionResultSet.getTimestamp("date").toInstant();
                    Money usedMoney = new Money(amount, transactionCurrency);

                    transactions.add(new Transaction(transactionId, typeValue, usedMoney, date, accountId.toString()));
                }
            }
        }
        return transactions;
    }

    @Override
    public void save(Account account) throws DatabaseOperationException {
        String query = "INSERT INTO Accounts (Id, AccountNumber, PinCode, Balance, Currency) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, account.id);
            statement.setString(2, account.accountNumber.toString());
            statement.setString(3, account.pinCode);
            statement.setBigDecimal(4, account.balance.getAmount());
            statement.setString(5, account.balance.getCurrency().name());
            statement.executeUpdate();
        } catch (SQLException ex) {
            throw new DatabaseOperationException("Failed to save account to the database.", ex);
        }
    }

    @Override
    public void update(Account account) throws SQLException, AccountNotFoundException {
        String query = "UPDATE Accounts SET Balance = ? WHERE AccountNumber = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setBigDecimal(1, account.balance.getAmount());
            statement.setString(2, account.accountNumber.toString());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new AccountNotFoundException(account.accountNumber.toString());
            }
        }
    }

    @Override
    public void delete(String accountNumber) throws SQLException, AccountNotFoundException {
        String query = "DELETE FROM Accounts WHERE AccountNumber = @AccountNumber";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, accountNumber);
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new AccountNotFoundException(accountNumber);
            }
        }
    }

    @Override
    public boolean verifyAccountPin(String accountNumber, String pin) throws SQLException, AccountNotFoundException, InvalidPinCodeException {
        String query = "SELECT pincode FROM Accounts WHERE accountnumber = ?";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, accountNumber);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (!resultSet.next()) {
                    System.out.println("No account found for number: " + accountNumber);
                    throw new AccountNotFoundException(accountNumber);
                }

                String storedPin = resultSet.getString("PinCode");

                if (!storedPin.equals(pin)) {
                    throw new InvalidPinCodeException();
                }
                return true;
            }
        }
    }

    public boolean checkAccountExistenceByNumber(String accountNumber) throws DataMappingException {
        String query = "SELECT * FROM accounts WHERE accountnumber = ? LIMIT 1";


        try (Connection connection = DriverManager.getConnection(connectionString)) {
            if (connection == null) {
                throw new SQLException("Database connection is null!");
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, accountNumber);

                try (ResultSet resultSet = statement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            throw new DataMappingException("Error checking account existence for number: " + accountNumber, e);
        }
    }
}
