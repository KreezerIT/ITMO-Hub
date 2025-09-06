package infrastructure.persistance;

import application.dtos.TransactionDTO;
import core.entities.Transaction;
import core.exceptions.DataMappingException;
import core.exceptions.TransactionFailedException;
import core.repositories.TransactionRepository;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import core.valueobjects.TransactionType;
import infrastructure.configs.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class TransactionRepositoryImpl implements TransactionRepository {
    private final String connectionString;

    public TransactionRepositoryImpl() {
        this.connectionString = AppConfig.getConnectionString();
    }

    public TransactionRepositoryImpl(String connectionString) {
        this.connectionString = connectionString;
    }

    public void save(TransactionDTO transaction) throws TransactionFailedException {
        String query = "INSERT INTO Transactions (Id, AccountId, Date, Type, Amount, Currency) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, transaction.id);
            statement.setObject(2, transaction.accountid);
            statement.setTimestamp(3, Timestamp.from(transaction.date));
            statement.setString(4, transaction.type.toString());
            statement.setBigDecimal(5, transaction.usedMoney.getAmount());
            statement.setString(6, transaction.usedMoney.getCurrency().toString());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new TransactionFailedException("Error saving transaction");
        }
    }

    public Collection<Transaction> getByAccountId(UUID accountId) throws DataMappingException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM Transactions WHERE AccountId = @AccountId";

        try (Connection connection = DriverManager.getConnection(connectionString);
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setObject(1, accountId);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                try {
                    Transaction transaction = new Transaction(
                            TransactionType.valueOf(resultSet.getString("Type")),
                            new Money(
                                    resultSet.getBigDecimal("Amount"),
                                    CurrencyType.valueOf(resultSet.getString("Currency"))
                            ),
                            resultSet.getTimestamp("Date").toInstant(),
                            resultSet.getString("AccountId")
                    );
                    transaction.id = UUID.fromString(resultSet.getString("Id"));
                    transaction.accountId = resultSet.getString("AccountId");

                    transactions.add(transaction);
                } catch (Exception ex) {
                    throw new DataMappingException("Error occurred while mapping transaction data.", ex);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Database error while retrieving transactions", e);
        }

        return transactions;
    }
}
