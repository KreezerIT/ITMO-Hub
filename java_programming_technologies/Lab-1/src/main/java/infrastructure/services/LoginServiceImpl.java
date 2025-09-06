package infrastructure.services;

import application.services.AccountServiceImpl;
import application.services.AuthenticationServiceImpl;
import application.services.IdentificationServiceImpl;
import core.exceptions.AccountNotFoundException;
import core.exceptions.DataMappingException;
import core.exceptions.InvalidInputException;
import core.exceptions.InvalidPinCodeException;
import infrastructure.interfaces.LoginService;

import java.sql.SQLException;
import java.util.Objects;

public class LoginServiceImpl implements LoginService {
    private final IdentificationServiceImpl identificationServiceImpl;
    private final AuthenticationServiceImpl authenticationServiceImpl;

    public LoginServiceImpl(IdentificationServiceImpl identificationServiceImpl, AuthenticationServiceImpl authenticationServiceImpl) {
        this.identificationServiceImpl = identificationServiceImpl;
        this.authenticationServiceImpl = authenticationServiceImpl;
    }

    public LoginServiceImpl(AccountServiceImpl accountServiceImpl) throws InvalidInputException {
        this.identificationServiceImpl = new IdentificationServiceImpl(accountServiceImpl);
        this.authenticationServiceImpl = new AuthenticationServiceImpl(accountServiceImpl);
    }

    public boolean loginUser(String accountNumber, String password) throws SQLException, DataMappingException, InvalidPinCodeException, AccountNotFoundException {
        Objects.requireNonNull(accountNumber, "Account number cannot be null");
        Objects.requireNonNull(password, "Password cannot be null");

        if (!identificationServiceImpl.isRegistered(accountNumber))
            throw new AccountNotFoundException("Account not found");
        return authenticationServiceImpl.authenticateUser(accountNumber, password);
    }

    public boolean loginAdmin(String password) {
        Objects.requireNonNull(password, "Password cannot be null");
        return authenticationServiceImpl.authenticateAdmin(password);
    }
}