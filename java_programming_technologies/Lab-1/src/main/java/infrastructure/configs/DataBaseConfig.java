package infrastructure.configs;

import core.exceptions.DataMappingException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConfig {
    private static String url = "jdbc:postgresql://localhost:5432/postgres?user=postgres&password=POSTitmoGRES12100";

    public static String getUrl() {
        return url;
    }

    public static void setUrl(String newUrl) {
        url = newUrl;
    }

    public static Connection getConnection() throws DataMappingException {
        try {
            return DriverManager.getConnection(url);
        } catch (SQLException e) {
            throw new DataMappingException("Database connection error: " + e.getMessage(), e);
        }
    }
}
