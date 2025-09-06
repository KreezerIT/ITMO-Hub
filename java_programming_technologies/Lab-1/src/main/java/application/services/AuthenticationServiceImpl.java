package application.services;

import application.interfaces.AuthenticationService;
import core.exceptions.AccountNotFoundException;
import core.exceptions.InvalidInputException;
import core.exceptions.InvalidPinCodeException;
import infrastructure.configs.AppConfig;

import java.sql.SQLException;

public class AuthenticationServiceImpl implements AuthenticationService {
    private final AccountServiceImpl accountServiceImpl;
    private final String adminPassword;

    public AuthenticationServiceImpl(String adminPassword, AccountServiceImpl accountServiceImpl) throws InvalidInputException {
        if (adminPassword == null || adminPassword.isEmpty()) {
            throw new InvalidInputException(adminPassword, "The admin password cannot be null or empty.");
        }
        this.adminPassword = adminPassword;
        this.accountServiceImpl = accountServiceImpl;
    }

    public AuthenticationServiceImpl(AccountServiceImpl accountServiceImpl) throws InvalidInputException {
        this(AppConfig.getAdminPassword(), accountServiceImpl);
    }

    public boolean authenticateUser(String accountNumber, String pin) throws SQLException, InvalidPinCodeException, AccountNotFoundException {
        return accountServiceImpl.verifyPin(accountNumber, pin);
    }

    public boolean authenticateAdmin(String inputPassword) {
        return adminPassword.equals(inputPassword);
    }
}
