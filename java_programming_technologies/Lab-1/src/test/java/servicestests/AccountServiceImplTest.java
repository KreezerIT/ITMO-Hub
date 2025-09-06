package servicestests;

import application.services.AccountServiceImpl;
import core.exceptions.InvalidAccountException;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import infrastructure.persistance.AccountRepositoryImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private AccountRepositoryImpl repository;

    @InjectMocks
    private AccountServiceImpl accountServiceImpl;

    private final String testAccountNumber = "12345678901234567890";
    private final UUID testAccountId = UUID.randomUUID();

    @Test
    void createAccount_ShouldThrowException_WhenCurrencyIsInvalid() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountServiceImpl.createAccount(testAccountNumber, "1234", "KOKO"));

        assertEquals("Invalid currency type: KOKO", exception.getMessage());
    }

    @Test
    void getBalance_ShouldThrowException_WhenAccountNotFound() throws SQLException {
        when(repository.getAccountByNumber(testAccountNumber)).thenReturn(null);

        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->
                accountServiceImpl.getBalance(testAccountNumber));

        assertEquals("The account with number " + testAccountNumber + " is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void deposit_ShouldThrowException_WhenAccountNotFound() throws SQLException {
        when(repository.getAccountByNumber(testAccountNumber)).thenReturn(null);

        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->
                accountServiceImpl.deposit(testAccountNumber, new Money(BigDecimal.TEN, CurrencyType.USD)));

        assertEquals("The account with number " + testAccountNumber + " is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void withdraw_ShouldThrowException_WhenAccountNotFound() throws SQLException {
        when(repository.getAccountByNumber(testAccountNumber)).thenReturn(null);

        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->
                accountServiceImpl.withdraw(testAccountNumber, 100));

        assertEquals("The account with number " + testAccountNumber + " is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void getDTOAccountDetails_ShouldThrowException_WhenAccountNotFound() throws SQLException {
        when(repository.getAccountByNumber(testAccountNumber)).thenReturn(null);

        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->
                accountServiceImpl.getDTOAccountDetails(testAccountNumber));

        assertEquals("The account with number " + testAccountNumber + " is invalid or does not exist.", exception.getMessage());
    }

    @Test
    void verifyPin_ShouldThrowException_WhenAccountNumberIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountServiceImpl.verifyPin(null, "1234"));

        assertEquals("Account number cannot be null or empty.", exception.getMessage());
    }

    @Test
    void verifyPin_ShouldThrowException_WhenPinIsEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountServiceImpl.verifyPin(testAccountNumber, ""));

        assertEquals("Pin cannot be null or empty.", exception.getMessage());
    }

    @Test
    void checkByAccountNumber_ShouldThrowException_WhenAccountNumberIsNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                accountServiceImpl.checkByAccountNumber(null));

        assertEquals("Account number cannot be null or empty.", exception.getMessage());
    }

    @Test
    void getAccountTransactionsByViewModel_ShouldThrowException_WhenAccountNotFound() throws SQLException {
        when(repository.getAccountByNumber(testAccountNumber)).thenReturn(null);

        InvalidAccountException exception = assertThrows(InvalidAccountException.class, () ->
                accountServiceImpl.getAccountTransactionsByViewModel(testAccountNumber));

        assertEquals("The account with number " + testAccountNumber + " is invalid or does not exist.", exception.getMessage());
    }
}