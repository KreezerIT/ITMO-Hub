package infrastructure.interfaces;

import core.exceptions.AccountNotFoundException;
import core.exceptions.DataMappingException;
import core.exceptions.InvalidPinCodeException;

import java.sql.SQLException;

public interface LoginService {
    boolean loginUser(String accountNumber, String password) throws SQLException, DataMappingException, InvalidPinCodeException, AccountNotFoundException;

    boolean loginAdmin(String password);
}
