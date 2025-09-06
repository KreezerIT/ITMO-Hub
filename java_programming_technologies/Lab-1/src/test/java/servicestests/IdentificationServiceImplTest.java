package servicestests;

import application.services.AccountServiceImpl;
import application.services.IdentificationServiceImpl;
import core.exceptions.DataMappingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
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
class IdentificationServiceImplTest {

    @Mock
    private AccountServiceImpl accountServiceImpl;

    @InjectMocks
    private IdentificationServiceImpl identificationServiceImpl;

    @Test
    void isRegistered_ShouldReturnTrue_WhenAccountExists() throws SQLException, DataMappingException {
        String accountNumber = "12345678901234567890";
        when(accountServiceImpl.checkByAccountNumber(accountNumber)).thenReturn(true);

        boolean result = identificationServiceImpl.isRegistered(accountNumber);

        assertTrue(result);
        verify(accountServiceImpl, times(1)).checkByAccountNumber(accountNumber);
    }

    @Test
    void isRegistered_ShouldReturnFalse_WhenAccountDoesNotExist() throws SQLException, DataMappingException {
        String accountNumber = "12345678901234567890";
        when(accountServiceImpl.checkByAccountNumber(accountNumber)).thenReturn(false);

        boolean result = identificationServiceImpl.isRegistered(accountNumber);

        assertFalse(result);
        verify(accountServiceImpl, times(1)).checkByAccountNumber(accountNumber);
    }

    @Test
    void isRegistered_ShouldThrowSQLException_WhenRepositoryFails() throws SQLException, DataMappingException {
        String accountNumber = "12345678901234567890";
        when(accountServiceImpl.checkByAccountNumber(accountNumber)).thenThrow(new SQLException("Database error"));

        SQLException exception = assertThrows(SQLException.class, () ->
                identificationServiceImpl.isRegistered(accountNumber)
        );

        assertEquals("Database error", exception.getMessage());
        verify(accountServiceImpl, times(1)).checkByAccountNumber(accountNumber);
    }
}
