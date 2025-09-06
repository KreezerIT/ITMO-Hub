package core.exceptions;

public class InvalidAccountException extends Exception {
    public InvalidAccountException(String message) {
        super("The account with number " + message + " is invalid or does not exist.");
    }
}