package core.exceptions;

public class InvalidCurrencyException extends Exception {
    public InvalidCurrencyException(String currency) {
        super("The currency '" + currency + "' is invalid or not supported.");
    }
}