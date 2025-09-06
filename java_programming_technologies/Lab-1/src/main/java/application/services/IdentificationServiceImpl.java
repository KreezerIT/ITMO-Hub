package application.services;

import application.interfaces.IdentificationService;
import core.exceptions.DataMappingException;

import java.sql.SQLException;

public class IdentificationServiceImpl implements IdentificationService {
    private final AccountServiceImpl accountServiceImpl;

    public IdentificationServiceImpl(AccountServiceImpl accountServiceImpl) {
        this.accountServiceImpl = accountServiceImpl;
    }

    public boolean isRegistered(String identifier) throws SQLException, DataMappingException {
        return accountServiceImpl.checkByAccountNumber(identifier);
    }
}
