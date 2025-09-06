package core.exceptions;

public class DataMappingException extends Exception {
    public DataMappingException(String message) {
        super(message);
    }

    public DataMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}