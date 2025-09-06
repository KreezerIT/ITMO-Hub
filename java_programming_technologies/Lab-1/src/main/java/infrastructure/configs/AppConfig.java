package infrastructure.configs;

import java.util.HashMap;
import java.util.Map;

public class AppConfig {
    private static final Map<String, String> commandLineArgs = new HashMap<>();

    public static String getConnectionString() {
        return commandLineArgs.getOrDefault("ConnectionString", DataBaseConfig.getUrl());
    }

    public static String getAdminPassword() {
        return commandLineArgs.getOrDefault("AdminPassword", "admin123");
    }

    public static String getLogFilePath() {
        return commandLineArgs.getOrDefault("LogFilePath", "D:\\Java_test.txt");
    }

    public static boolean isEnableConsoleLogging() {
        return Boolean.parseBoolean(commandLineArgs.getOrDefault("EnableConsoleLogging", "false"));
    }

    public static boolean isEnableFileLogging() {
        return Boolean.parseBoolean(commandLineArgs.getOrDefault("EnableFileLogging", "false"));
    }
}

