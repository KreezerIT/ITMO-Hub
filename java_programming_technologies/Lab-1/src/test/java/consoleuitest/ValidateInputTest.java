package consoleuitest;

import infrastructure.presentation.consoleui.ValidateInput;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ValidateInputTest {

    @Test
    void getValidatedInput_ShouldReturnValidInput_WhenInputMatchesRegex() {
        String regex = "\\d+";
        String expectedInput = "12345\n";
        InputStream originalSystemIn = System.in;
        PrintStream originalSystemOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(expectedInput.getBytes()));

            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            String result = ValidateInput.getValidatedInput(regex, "Input error!");
            assertEquals("12345", result);
        } finally {
            System.setIn(originalSystemIn);
            System.setOut(originalSystemOut);
        }
    }

    @Test
    void getValidatedInput_ShouldRetryUntilValidInput_WhenInputDoesNotMatchRegex() {
        String regex = "\\d+";
        String userInput = "abc\nabc\n6789\n";
        InputStream originalSystemIn = System.in;
        PrintStream originalSystemOut = System.out;

        try {
            System.setIn(new ByteArrayInputStream(userInput.getBytes()));

            System.setOut(new PrintStream(new ByteArrayOutputStream()));

            String result = ValidateInput.getValidatedInput(regex, "Input error!");
            assertEquals("6789", result);
        } finally {
            System.setIn(originalSystemIn);
            System.setOut(originalSystemOut);
        }
    }
}
