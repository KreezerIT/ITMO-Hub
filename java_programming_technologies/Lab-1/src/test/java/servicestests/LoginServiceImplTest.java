package servicestests;

import application.services.AuthenticationServiceImpl;
import application.services.IdentificationServiceImpl;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DataMappingException;
import core.exceptions.InvalidPinCodeException;
import infrastructure.services.LoginServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.anyString;

class LoginServiceImplTest {

    @Mock
    private IdentificationServiceImpl mockIdentificationServiceImpl;

    @Mock
    private AuthenticationServiceImpl mockAuthenticationServiceImpl;

    private LoginServiceImpl loginServiceImpl;

    private final String validAccountNumber = "1234567890123456";
    private final String validPassword = "securePassword";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginServiceImpl = new LoginServiceImpl(mockIdentificationServiceImpl, mockAuthenticationServiceImpl);
    }

    @Test
    void loginUser_ShouldReturnTrue_WhenCredentialsAreValid() throws SQLException, AccountNotFoundException, DataMappingException, InvalidPinCodeException {
        when(mockIdentificationServiceImpl.isRegistered(validAccountNumber)).thenReturn(true);
        when(mockAuthenticationServiceImpl.authenticateUser(validAccountNumber, validPassword)).thenReturn(true);

        boolean result = loginServiceImpl.loginUser(validAccountNumber, validPassword);

        assertTrue(result);
        verify(mockIdentificationServiceImpl).isRegistered(validAccountNumber);
        verify(mockAuthenticationServiceImpl).authenticateUser(validAccountNumber, validPassword);
    }

    @Test
    void loginUser_ShouldThrowAccountNotFoundException_WhenAccountIsNotRegistered() throws SQLException, AccountNotFoundException, DataMappingException, InvalidPinCodeException {
        when(mockIdentificationServiceImpl.isRegistered(validAccountNumber)).thenReturn(false);

        AccountNotFoundException exception = assertThrows(AccountNotFoundException.class, () ->
                loginServiceImpl.loginUser(validAccountNumber, validPassword)
        );

        assertEquals("Account not found", exception.getMessage());
        verify(mockIdentificationServiceImpl).isRegistered(validAccountNumber);
        verify(mockAuthenticationServiceImpl, never()).authenticateUser(anyString(), anyString());
    }

    @Test
    void loginUser_ShouldThrowNullPointerException_WhenAccountNumberIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                loginServiceImpl.loginUser(null, validPassword)
        );

        assertEquals("Account number cannot be null", exception.getMessage());
    }

    @Test
    void loginUser_ShouldThrowNullPointerException_WhenPasswordIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                loginServiceImpl.loginUser(validAccountNumber, null)
        );

        assertEquals("Password cannot be null", exception.getMessage());
    }

    @Test
    void loginAdmin_ShouldReturnTrue_WhenAdminPasswordIsValid() {
        when(mockAuthenticationServiceImpl.authenticateAdmin(validPassword)).thenReturn(true);

        boolean result = loginServiceImpl.loginAdmin(validPassword);

        assertTrue(result);
        verify(mockAuthenticationServiceImpl).authenticateAdmin(validPassword);
    }

    @Test
    void loginAdmin_ShouldReturnFalse_WhenAdminPasswordIsInvalid() {
        when(mockAuthenticationServiceImpl.authenticateAdmin("wrongPassword")).thenReturn(false);

        boolean result = loginServiceImpl.loginAdmin("wrongPassword");

        assertFalse(result);
        verify(mockAuthenticationServiceImpl).authenticateAdmin("wrongPassword");
    }

    @Test
    void loginAdmin_ShouldThrowNullPointerException_WhenPasswordIsNull() {
        NullPointerException exception = assertThrows(NullPointerException.class, () ->
                loginServiceImpl.loginAdmin(null)
        );

        assertEquals("Password cannot be null", exception.getMessage());
    }
}
