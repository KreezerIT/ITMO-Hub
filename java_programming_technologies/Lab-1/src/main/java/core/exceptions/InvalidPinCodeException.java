package core.exceptions;

public class InvalidPinCodeException extends Exception {
    public InvalidPinCodeException() {
        super("The provided PIN code is invalid.");
    }
}