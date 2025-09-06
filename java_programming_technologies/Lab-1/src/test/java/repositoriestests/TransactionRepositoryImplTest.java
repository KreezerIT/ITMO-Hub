package repositoriestests;

import core.exceptions.DataMappingException;
import core.exceptions.TransactionFailedException;
import infrastructure.persistance.TransactionRepositoryImpl;
import application.dtos.TransactionDTO;
import core.entities.Transaction;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import core.valueobjects.TransactionType;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class TransactionRepositoryImplTest {

    private TransactionRepositoryImpl transactionRepositoryImpl;
    private MockedStatic<DriverManager> mockedDriverManager;
    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;

    private final UUID testAccountId = UUID.randomUUID();
    private final UUID testTransactionId = UUID.randomUUID();
    private final Money testMoney = new Money(new BigDecimal("50.00"), CurrencyType.USD);
    private final TransactionType testTransactionType = TransactionType.ACCOUNT_REPLENISHMENT;
    private final Instant testDate = Instant.now();

    @BeforeEach
    void setUp() throws Exception {
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        mockedDriverManager = mockStatic(DriverManager.class);
        mockedDriverManager.when(() -> DriverManager.getConnection(anyString())).thenReturn(mockConnection);

        lenient().when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        lenient().when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        transactionRepositoryImpl = new TransactionRepositoryImpl("jdbc:postgresql://localhost:5432/testdb");
    }


    @AfterEach
    void tearDown() {
        if (mockedDriverManager != null) {
            mockedDriverManager.close();
        }
    }

    @Test
    void save_ShouldThrowException_WhenSQLExceptionOccurs() throws Exception {
        TransactionDTO testTransaction = new TransactionDTO(testTransactionId, testTransactionType, testMoney, testDate, testAccountId);
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doThrow(new SQLException("SQL error")).when(mockStatement).executeUpdate();

        TransactionFailedException exception = assertThrows(TransactionFailedException.class, () ->
                transactionRepositoryImpl.save(testTransaction));

        assertTrue(exception.getMessage().contains("Error saving transaction"));
    }

    @Test
    void getByAccountId_ShouldReturnEmptyCollection_WhenNoTransactionsExist() throws Exception {
        when(mockResultSet.next()).thenReturn(false);

        Collection<Transaction> transactions = transactionRepositoryImpl.getByAccountId(testAccountId);

        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }

    @Test
    void getByAccountId_ShouldReturnTransactions_WhenTransactionsExist() throws Exception {
        UUID testAccountId = UUID.randomUUID();
        UUID transactionId = UUID.randomUUID();

        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getString("Id")).thenReturn(transactionId.toString());
        when(mockResultSet.getString("AccountId")).thenReturn(testAccountId.toString());
        when(mockResultSet.getString("Type")).thenReturn("ACCOUNT_REPLENISHMENT");
        when(mockResultSet.getBigDecimal("Amount")).thenReturn(new BigDecimal("100.00"));
        when(mockResultSet.getString("Currency")).thenReturn("USD");
        Timestamp testTimestamp = Timestamp.from(Instant.now());
        when(mockResultSet.getTimestamp("Date")).thenReturn(testTimestamp);

        Collection<Transaction> transactions = transactionRepositoryImpl.getByAccountId(testAccountId);

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }


    @Test
    void getByAccountId_ShouldThrowDataMappingException_WhenMappingFails() throws Exception {
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("Type")).thenReturn("INVALID_TYPE");

        DataMappingException exception = assertThrows(DataMappingException.class, () ->
                transactionRepositoryImpl.getByAccountId(testAccountId));

        assertTrue(exception.getMessage().contains("Error occurred while mapping transaction data"));
    }

    @Test
    void getByAccountId_ShouldThrowRuntimeException_WhenSQLExceptionOccurs() throws Exception {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        doThrow(new SQLException("SQL error")).when(mockStatement).executeQuery();

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                transactionRepositoryImpl.getByAccountId(testAccountId));

        assertTrue(exception.getMessage().contains("Database error while retrieving transactions"));
    }
}
