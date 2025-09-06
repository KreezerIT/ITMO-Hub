package core.exceptions;

public class TransactionFailedException extends Exception {
    public TransactionFailedException(String message) {
        super("Transaction failed: " + message);
    }
}