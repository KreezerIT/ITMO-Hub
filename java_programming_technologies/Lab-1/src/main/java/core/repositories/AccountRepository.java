package core.repositories;

import core.entities.Account;
import core.entities.Transaction;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DatabaseOperationException;
import core.exceptions.InvalidPinCodeException;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface AccountRepository {

    Account getAccountByNumber(String accountNumber) throws SQLException;

    List<Transaction> loadTransactions(UUID accountId, Connection connection) throws SQLException;

    void update(Account account) throws SQLException, AccountNotFoundException;

    void save(Account account) throws SQLException, DatabaseOperationException;

    void delete(String accountNumber) throws SQLException, AccountNotFoundException;

    boolean verifyAccountPin(String accountNumber, String pin) throws SQLException, AccountNotFoundException, InvalidPinCodeException;
}