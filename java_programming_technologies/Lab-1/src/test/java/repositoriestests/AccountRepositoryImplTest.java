package repositoriestests;

import core.entities.Transaction;
import core.exceptions.DataMappingException;
import infrastructure.persistance.AccountRepositoryImpl;
import core.entities.Account;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DatabaseOperationException;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class AccountRepositoryImplTest {

    private AccountRepositoryImpl accountRepositoryImpl;
    private MockedStatic<DriverManager> mockedDriverManager;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    private final Money testBalance = new Money(new BigDecimal("100.00"), CurrencyType.USD);
    private final UUID testAccountId = UUID.randomUUID();
    private final UUID testTransactionId = UUID.randomUUID();
    private final String testAccountNumberNotExist = "12345678901234567890";
    private final String testAccountNumberExist = "11111111111111111111";

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedDriverManager = mockStatic(DriverManager.class);
        mockedDriverManager.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

        lenient().when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        lenient().when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        lenient().when(mockResultSet.next()).thenReturn(false);

        accountRepositoryImpl = new AccountRepositoryImpl("jdbc:postgresql://localhost:5432/testdb");
    }

    @AfterEach
    void tearDown() {
        if (mockedDriverManager != null) {
            mockedDriverManager.close();
        }
    }

    @Test
    void save_ShouldThrowException_WhenSQLExceptionOccurs() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        doThrow(new SQLException("SQL error")).when(mockStatement).executeUpdate();

        Account testAccount = new Account(testAccountId, testAccountNumberNotExist, "1234", testBalance, null);

        DatabaseOperationException exception = assertThrows(DatabaseOperationException.class, () ->
                accountRepositoryImpl.save(testAccount));

        assertTrue(exception.getMessage().contains("Failed to save account"));
    }


    @Test
    void update_ShouldThrowAccountNotFoundException_WhenNoRowsAffected() throws Exception {
        when(mockStatement.executeUpdate()).thenReturn(0);

        Account testAccount = new Account(testAccountId, testAccountNumberNotExist, "1234", testBalance, null);

        assertThrows(AccountNotFoundException.class, () ->
                accountRepositoryImpl.update(testAccount));
    }

    @Test
    void delete_ShouldThrowAccountNotFoundException_WhenAccountDoesNotExist() throws Exception {
        when(mockStatement.executeUpdate()).thenReturn(0);

        assertThrows(AccountNotFoundException.class, () ->
                accountRepositoryImpl.delete(testAccountNumberNotExist));
    }

    @Test
    void checkAccountExistenceByNumber_ShouldReturnTrue_WhenAccountExists() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);

        assertTrue(accountRepositoryImpl.checkAccountExistenceByNumber(testAccountNumberExist));
    }

    @Test
    void checkAccountExistenceByNumber_ShouldReturnFalse_WhenAccountDoesNotExist() throws Exception {
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        assertFalse(accountRepositoryImpl.checkAccountExistenceByNumber(testAccountNumberNotExist));
    }

    @Test
    void checkAccountExistenceByNumber_ShouldThrowDataMappingException_WhenSQLExceptionOccurs() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        doThrow(new SQLException("SQL error")).when(mockStatement).executeQuery();

        DataMappingException exception = assertThrows(DataMappingException.class, () ->
                accountRepositoryImpl.checkAccountExistenceByNumber(testAccountNumberNotExist)
        );

        assertTrue(exception.getMessage().contains("Error checking account existence"));
    }

    @Test
    void loadTransactions_ShouldReturnTransactionList_WhenTransactionsExist() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("id")).thenReturn(testTransactionId.toString());
        when(mockResultSet.getBigDecimal("amount")).thenReturn(new BigDecimal("100.00"));
        when(mockResultSet.getString("currency")).thenReturn("USD");
        when(mockResultSet.getString("type")).thenReturn("ACCOUNT_REPLENISHMENT");
        when(mockResultSet.getTimestamp("date")).thenReturn(Timestamp.from(Instant.now()));

        List<Transaction> transactions = accountRepositoryImpl.loadTransactions(testAccountId, mockConnection);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
        assertEquals(testTransactionId, transactions.getFirst().id);
    }
}
