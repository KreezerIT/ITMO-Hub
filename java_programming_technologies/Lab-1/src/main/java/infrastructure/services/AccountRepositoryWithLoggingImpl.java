package infrastructure.services;

import core.entities.Account;
import core.entities.Transaction;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DatabaseOperationException;
import core.exceptions.InvalidPinCodeException;
import core.repositories.AccountRepository;
import infrastructure.interfaces.Logger;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class AccountRepositoryWithLoggingImpl implements AccountRepository {
    private final AccountRepository innerRepository;
    private final Logger logger;

    public AccountRepositoryWithLoggingImpl(AccountRepository innerRepository, Logger logger) {
        this.innerRepository = innerRepository;
        this.logger = logger;
    }

    public Account getAccountByNumber(String accountNumber) throws SQLException {
        logger.log("Fetching account with AccountNumber: " + accountNumber);
        Account result = innerRepository.getAccountByNumber(accountNumber);
        logger.log(result != null
                ? "Account with AccountNumber: " + accountNumber + " fetched successfully."
                : "Account with AccountNumber: " + accountNumber + " not found.");
        return result;
    }

    public List<Transaction> loadTransactions(UUID accountId, Connection connection) throws SQLException {
        logger.log("Fetching transactions of account with AccountNumber: " + accountId);
        return innerRepository.loadTransactions(accountId, connection);
    }

    public void save(Account account) throws SQLException, DatabaseOperationException {
        logger.log("Saving account with AccountNumber: " + account.accountNumber);
        innerRepository.save(account);
        logger.log("Account with AccountNumber: " + account.accountNumber + " saved successfully.");
    }

    public void update(Account account) throws SQLException, AccountNotFoundException {
        logger.log("Updating account with AccountNumber: " + account.accountNumber);
        innerRepository.update(account);
        logger.log("Account with AccountNumber: " + account.accountNumber + " updated successfully.");
    }

    public void delete(String accountNumber) throws SQLException, AccountNotFoundException {
        logger.log("Deleting account with AccountNumber: " + accountNumber);
        innerRepository.delete(accountNumber);
        logger.log("Account with AccountNumber: " + accountNumber + " deleted successfully.");
    }

    public boolean verifyAccountPin(String accountNumber, String pin) throws SQLException, InvalidPinCodeException, AccountNotFoundException {
        logger.log("Verifying PIN for AccountNumber: " + accountNumber);
        boolean result = innerRepository.verifyAccountPin(accountNumber, pin);
        logger.log(result
                ? "PIN verification succeeded for AccountNumber: " + accountNumber
                : "PIN verification failed for AccountNumber: " + accountNumber);
        return result;
    }
}