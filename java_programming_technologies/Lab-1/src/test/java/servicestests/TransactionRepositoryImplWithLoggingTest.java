package servicestests;

import application.dtos.TransactionDTO;
import core.entities.Transaction;
import core.exceptions.DataMappingException;
import core.exceptions.TransactionFailedException;
import core.repositories.TransactionRepository;
import infrastructure.interfaces.Logger;
import infrastructure.services.TransactionRepositoryWithLoggingImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.when;

class TransactionRepositoryImplWithLoggingTest {

    @Mock
    private TransactionRepository mockInnerRepository;

    @Mock
    private Logger mockLogger;

    private TransactionRepositoryWithLoggingImpl transactionRepositoryWithLoggingImpl;
    private UUID testAccountId;
    private UUID testTransactionId;
    private TransactionDTO testTransactionDTO;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        transactionRepositoryWithLoggingImpl = new TransactionRepositoryWithLoggingImpl(mockInnerRepository, mockLogger);

        testAccountId = UUID.randomUUID();
        testTransactionId = UUID.randomUUID();

        testTransactionDTO = new TransactionDTO(
                testTransactionId,
                null,
                null,
                null,
                testAccountId
        );

        testTransaction = new Transaction(
                testTransactionId,
                null,
                null,
                null,
                testAccountId.toString()
        );
    }

    @Test
    void save_ShouldLogMessagesAndCallInnerRepository() throws TransactionFailedException {
        transactionRepositoryWithLoggingImpl.save(testTransactionDTO);

        InOrder inOrder = inOrder(mockLogger, mockInnerRepository);

        inOrder.verify(mockLogger).log("Saving transaction with Id: " + testTransactionDTO.id + ", AccountId: " + testTransactionDTO.accountid);
        inOrder.verify(mockInnerRepository).save(testTransactionDTO);
        inOrder.verify(mockLogger).log("Transaction with Id: " + testTransactionDTO.id + " saved successfully.");
    }

    @Test
    void getByAccountId_ShouldLogMessagesAndReturnTransactions() throws DataMappingException {
        List<Transaction> expectedTransactions = Collections.singletonList(testTransaction);
        when(mockInnerRepository.getByAccountId(testAccountId)).thenReturn(expectedTransactions);

        Collection<Transaction> result = transactionRepositoryWithLoggingImpl.getByAccountId(testAccountId);

        assertEquals(expectedTransactions, result);

        InOrder inOrder = inOrder(mockLogger, mockInnerRepository);
        inOrder.verify(mockLogger).log("Fetching transactions for AccountId: " + testAccountId);
        inOrder.verify(mockInnerRepository).getByAccountId(testAccountId);
        inOrder.verify(mockLogger).log("Fetched " + expectedTransactions.size() + " transactions for AccountId: " + testAccountId);
    }
}
