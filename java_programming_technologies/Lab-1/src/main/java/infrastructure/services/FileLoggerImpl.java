package infrastructure.services;

import infrastructure.configs.AppConfig;
import infrastructure.interfaces.Logger;

import java.io.FileWriter;
import java.io.IOException;

public class FileLoggerImpl implements Logger {
    private final String filePath;

    public FileLoggerImpl(String filePath) {
        this.filePath = filePath;
    }

    public FileLoggerImpl() {
        this.filePath = AppConfig.getLogFilePath();
    }

    public void log(String message) {
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("[FileLogger] " + message + System.lineSeparator());
        } catch (IOException e) {
            throw new RuntimeException("Failed to write log to file", e);
        }
    }
}