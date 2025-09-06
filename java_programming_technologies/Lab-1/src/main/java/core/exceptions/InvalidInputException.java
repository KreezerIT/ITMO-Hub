package core.exceptions;

public class InvalidInputException extends Exception {
    public InvalidInputException(String input, String reason) {
        super("The input '" + input + "' is invalid: " + reason);
    }
}