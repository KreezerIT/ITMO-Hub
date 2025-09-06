package servicestests;

import application.dtos.AccountDTO;
import application.dtos.TransactionDTO;
import core.entities.Account;
import core.entities.Transaction;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DatabaseOperationException;
import core.exceptions.InvalidPinCodeException;
import core.repositories.AccountRepository;
import infrastructure.interfaces.Logger;
import core.valueobjects.Money;
import core.valueobjects.CurrencyType;
import infrastructure.services.AccountRepositoryWithLoggingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

class AccountRepositoryImplWithLoggingTest {

    private AccountRepository mockRepository;
    private Logger mockLogger;
    private AccountRepositoryWithLoggingImpl repositoryWithLogging;
    private UUID accountId;
    private String accountNumber;
    private Account testAccount;
    private AccountDTO testAccountDTO;
    private Transaction testTransaction;
    private TransactionDTO testTransactionDTO;

    @BeforeEach
    void setUp() {
        mockRepository = mock(AccountRepository.class);
        mockLogger = mock(Logger.class);
        repositoryWithLogging = new AccountRepositoryWithLoggingImpl(mockRepository, mockLogger);

        accountId = UUID.randomUUID();
        accountNumber = "123456";
        Money balance = new Money(BigDecimal.valueOf(1000), CurrencyType.USD);

        testAccount = new Account(accountId, accountNumber, "1234", balance, Collections.emptyList());
        testAccountDTO = new AccountDTO(accountId, accountNumber, balance, Collections.emptyList(), "1234");
        testTransaction = new Transaction(UUID.randomUUID(), null, balance, Instant.now(), accountNumber);
        testTransactionDTO = new TransactionDTO(UUID.randomUUID(), null, balance, Instant.now(), accountId);
    }

    @Test
    void getAccountbyNumber_ShouldLogAndReturnAccount() throws SQLException {
        when(mockRepository.getAccountByNumber(accountNumber)).thenReturn(testAccount);

        Account result = repositoryWithLogging.getAccountByNumber(accountNumber);

        assertEquals(testAccount, result);
        verify(mockLogger).log("Fetching account with AccountNumber: " + accountNumber);
        verify(mockRepository).getAccountByNumber(accountNumber);
    }

    @Test
    void loadTransactions_ShouldLogAndReturnTransactions() throws SQLException {
        Connection mockConnection = mock(Connection.class);
        List<Transaction> transactions = List.of(testTransaction);
        when(mockRepository.loadTransactions(accountId, mockConnection)).thenReturn(transactions);

        List<Transaction> result = repositoryWithLogging.loadTransactions(accountId, mockConnection);

        assertEquals(transactions, result);
        verify(mockLogger).log("Fetching transactions of account with AccountNumber: " + accountId);
        verify(mockRepository).loadTransactions(accountId, mockConnection);
    }

    @Test
    void save_ShouldLogBeforeAndAfterSaving() throws SQLException, DatabaseOperationException {
        repositoryWithLogging.save(testAccount);

        verify(mockLogger).log("Saving account with AccountNumber: " + accountNumber);
        verify(mockRepository).save(testAccount);
        verify(mockLogger).log("Account with AccountNumber: " + accountNumber + " saved successfully.");
    }

    @Test
    void update_ShouldLogBeforeAndAfterUpdating() throws SQLException, AccountNotFoundException {
        repositoryWithLogging.update(testAccount);

        verify(mockLogger).log("Updating account with AccountNumber: " + accountNumber);
        verify(mockRepository).update(testAccount);
        verify(mockLogger).log("Account with AccountNumber: " + accountNumber + " updated successfully.");
    }

    @Test
    void delete_ShouldLogBeforeAndAfterDeleting() throws SQLException, AccountNotFoundException {
        repositoryWithLogging.delete(accountNumber);

        verify(mockLogger).log("Deleting account with AccountNumber: " + accountNumber);
        verify(mockRepository).delete(accountNumber);
        verify(mockLogger).log("Account with AccountNumber: " + accountNumber + " deleted successfully.");
    }

    @Test
    void verifyAccountPin_ShouldLogAndReturnResult() throws SQLException, InvalidPinCodeException, AccountNotFoundException {
        when(mockRepository.verifyAccountPin(accountNumber, "1234")).thenReturn(true);

        boolean result = repositoryWithLogging.verifyAccountPin(accountNumber, "1234");

        assertTrue(result);
        verify(mockLogger).log("Verifying PIN for AccountNumber: " + accountNumber);
        verify(mockRepository).verifyAccountPin(accountNumber, "1234");
        verify(mockLogger).log("PIN verification succeeded for AccountNumber: " + accountNumber);
    }
}
