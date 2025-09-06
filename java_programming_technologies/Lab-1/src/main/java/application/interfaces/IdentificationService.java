package application.interfaces;

import core.exceptions.DataMappingException;

import java.sql.SQLException;

public interface IdentificationService {
    boolean isRegistered(String identifier) throws SQLException, DataMappingException;
}
