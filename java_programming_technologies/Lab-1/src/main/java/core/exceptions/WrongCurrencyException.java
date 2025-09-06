package core.exceptions;

public class WrongCurrencyException extends Exception {
    public WrongCurrencyException(String currencyOne, String currencyTwo) {
        super("The currency '" + currencyOne + "' is not equals '" + currencyTwo + "'");
    }
}
