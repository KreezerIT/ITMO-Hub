package application.interfaces;

import core.exceptions.AccountNotFoundException;
import core.exceptions.InvalidPinCodeException;

import java.sql.SQLException;

public interface AuthenticationService {
    boolean authenticateUser(String accountNumber, String pin) throws SQLException, InvalidPinCodeException, AccountNotFoundException;

    boolean authenticateAdmin(String inputPassword);
}
