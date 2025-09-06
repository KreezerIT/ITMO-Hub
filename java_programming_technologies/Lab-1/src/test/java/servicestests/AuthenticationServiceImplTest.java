package servicestests;

import application.services.AccountServiceImpl;
import application.services.AuthenticationServiceImpl;
import core.exceptions.AccountNotFoundException;
import core.exceptions.InvalidInputException;
import core.exceptions.InvalidPinCodeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {

    @Mock
    private AccountServiceImpl accountServiceImpl;

    private AuthenticationServiceImpl authenticationServiceImpl;

    @BeforeEach
    void setUp() throws InvalidInputException {
        authenticationServiceImpl = new AuthenticationServiceImpl("admin123", accountServiceImpl);
    }

    @Test
    void constructor_ShouldThrowException_WhenAdminPasswordIsNull() {
        Exception exception = assertThrows(InvalidInputException.class, () ->
                new AuthenticationServiceImpl(null, accountServiceImpl));

        assertEquals("The input 'null' is invalid: The admin password cannot be null or empty.", exception.getMessage());
    }

    @Test
    void constructor_ShouldThrowException_WhenAdminPasswordIsEmpty() {
        Exception exception = assertThrows(InvalidInputException.class, () ->
                new AuthenticationServiceImpl("", accountServiceImpl));

        assertEquals("The input '' is invalid: The admin password cannot be null or empty.", exception.getMessage());
    }

    @Test
    void authenticateUser_ShouldCallVerifyPin_OnAccountService() throws SQLException, AccountNotFoundException, InvalidPinCodeException {
        when(accountServiceImpl.verifyPin("12345678901234567890", "1234")).thenReturn(true);

        boolean result = authenticationServiceImpl.authenticateUser("12345678901234567890", "1234");

        assertTrue(result);
        verify(accountServiceImpl, times(1)).verifyPin("12345678901234567890", "1234");
    }

    @Test
    void authenticateAdmin_ShouldReturnTrue_WhenPasswordMatches() throws InvalidInputException {
        AuthenticationServiceImpl service = new AuthenticationServiceImpl("admin123", accountServiceImpl);
        assertTrue(service.authenticateAdmin("admin123"));
    }

    @Test
    void authenticateAdmin_ShouldReturnFalse_WhenPasswordDoesNotMatch() throws InvalidInputException {
        AuthenticationServiceImpl service = new AuthenticationServiceImpl("admin123", accountServiceImpl);
        assertFalse(service.authenticateAdmin("wrongpassword"));
    }
}
