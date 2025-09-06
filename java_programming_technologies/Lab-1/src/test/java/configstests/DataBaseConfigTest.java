package configstests;

import core.exceptions.DataMappingException;
import infrastructure.configs.DataBaseConfig;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;


class DataBaseConfigTest {

    @Test
    void testGetUrl() {
        String expectedUrl = DataBaseConfig.getUrl();
        assertEquals(expectedUrl, DataBaseConfig.getUrl(), "URL does not match expected");
    }

    @Test
    void testGetConnection_Success() throws DataMappingException {
        try (Connection connection = DataBaseConfig.getConnection()) {
            assertNotNull(connection, "Connection must be not null");
            assertFalse(connection.isClosed(), "Connection must be open");
        } catch (SQLException e) {
            fail("Error establishing connection " + e.getMessage());
        }
    }

    @Test
    void testGetConnection_Failure() {
        String originalUrl = DataBaseConfig.getUrl();

        try {
            DataBaseConfig.setUrl("jdbc:postgresql://invalidhost:5432/wrong_db");

            DataMappingException exception = assertThrows(DataMappingException.class, DataBaseConfig::getConnection);

            assertTrue(exception.getMessage().contains("Database connection error"));
        } finally {
            DataBaseConfig.setUrl(originalUrl);
        }
    }
}
