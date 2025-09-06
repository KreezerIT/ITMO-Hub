package servicestests;

import application.dtos.TransactionDTO;
import application.services.TransactionServiceImpl;
import core.entities.Account;
import core.exceptions.AccountNotFoundException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidOperationException;
import core.exceptions.TransactionFailedException;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import infrastructure.persistance.AccountRepositoryImpl;
import infrastructure.persistance.TransactionRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceImplTest {

    @Mock
    private AccountRepositoryImpl accountRepositoryImpl;

    @Mock
    private TransactionRepositoryImpl transactionRepositoryImpl;

    @InjectMocks
    private TransactionServiceImpl transactionServiceImpl;

    private Account testAccount;

    @BeforeEach
    void setUp() {
        testAccount = new Account("12345678901234567890", "1234", CurrencyType.USD);
        testAccount.setBalance(new BigDecimal("1000.00"));
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenValidDeposit() throws SQLException, AccountNotFoundException, InvalidOperationException, TransactionFailedException {
        Money depositAmount = new Money(new BigDecimal("500.00"), testAccount.balance.getCurrency());

        transactionServiceImpl.deposit(testAccount, depositAmount);

        assertEquals(new BigDecimal("1500.00"), testAccount.balance.getAmount());
        verify(transactionRepositoryImpl, times(1)).save(any(TransactionDTO.class));
        verify(accountRepositoryImpl, times(1)).update(testAccount);
    }

    @Test
    void deposit_ShouldThrowException_WhenCurrencyMismatch() {
        Money depositAmount = new Money(new BigDecimal("500.00"), CurrencyType.RUB);

        InvalidOperationException exception = assertThrows(InvalidOperationException.class, () ->
                transactionServiceImpl.deposit(testAccount, depositAmount));

        assertEquals("Currency mismatch.", exception.getMessage());
    }

    @Test
    void withdraw_ShouldDecreaseBalance_WhenSufficientFunds() throws SQLException, AccountNotFoundException, InvalidOperationException, InsufficientFundsException, TransactionFailedException {
        Money withdrawAmount = new Money(new BigDecimal("200.00"), testAccount.balance.getCurrency());

        transactionServiceImpl.withdraw(testAccount, withdrawAmount);

        assertEquals(new BigDecimal("800.00"), testAccount.balance.getAmount());
        verify(transactionRepositoryImpl, times(1)).save(any(TransactionDTO.class));
        verify(accountRepositoryImpl, times(1)).update(testAccount);
    }

    @Test
    void withdraw_ShouldThrowException_WhenInsufficientFunds() {
        Money withdrawAmount = new Money(new BigDecimal("1200.00"), testAccount.balance.getCurrency());

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class, () ->
                transactionServiceImpl.withdraw(testAccount, withdrawAmount));

        assertTrue(exception.getMessage().contains("Insufficient funds"));
    }

    @Test
    void getDTOTransactionHistory_ShouldThrowException_WhenAccountIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionServiceImpl.getDTOTransactionHistory(null));

        assertEquals("Account cannot be null.", exception.getMessage());
    }

    @Test
    void getTransactionHistoryByViewModel_ShouldThrowException_WhenAccountIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                transactionServiceImpl.getTransactionHistoryByViewModel(null));

        assertEquals("Account cannot be null.", exception.getMessage());
    }
}
