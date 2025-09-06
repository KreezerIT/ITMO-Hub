package infrastructure.services;

import infrastructure.interfaces.Logger;

public class ConsoleLoggerImpl implements Logger {
    public void log(String message) {
        System.out.println("[ConsoleLogger] " + message);
    }
}
