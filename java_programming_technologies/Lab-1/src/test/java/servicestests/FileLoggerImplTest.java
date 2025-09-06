package servicestests;

import infrastructure.interfaces.Logger;
import infrastructure.services.FileLoggerImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

class FileLoggerImplTest {

    private Path tempLogFile;
    private Logger fileLogger;

    @BeforeEach
    void setUp() throws IOException {
        tempLogFile = Files.createTempFile("test_log", ".txt");
        fileLogger = new FileLoggerImpl(tempLogFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempLogFile);
    }

    @Test
    void log_ShouldWriteMessageToFile() throws IOException {
        String testMessage = "Test log message";

        fileLogger.log(testMessage);

        List<String> lines = Files.readAllLines(tempLogFile);
        assertFalse(lines.isEmpty(), "Файл логов должен содержать данные");
        assertTrue(lines.getFirst().contains("[FileLogger] " + testMessage), "Лог должен содержать правильное сообщение");
    }

    @Test
    void log_ShouldAppendMessagesToFile() throws IOException {
        String message1 = "First log message";
        String message2 = "Second log message";

        fileLogger.log(message1);
        fileLogger.log(message2);

        List<String> lines = Files.readAllLines(tempLogFile);
        assertEquals(2, lines.size(), "Файл должен содержать две строки");
        assertTrue(lines.get(0).contains("[FileLogger] " + message1));
        assertTrue(lines.get(1).contains("[FileLogger] " + message2));
    }

    @Test
    void log_ShouldThrowException_WhenFileNotWritable() {
        File file = tempLogFile.toFile();
        file.setReadOnly();

        try {
            assertThrows(RuntimeException.class, () -> fileLogger.log("This should fail"),
                    "Должно выбрасываться исключение при попытке записи в файл без прав на запись");
        } finally {
            file.setWritable(true);
        }
    }
}
