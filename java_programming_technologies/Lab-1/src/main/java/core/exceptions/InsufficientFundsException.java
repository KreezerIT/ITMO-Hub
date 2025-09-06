package core.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException(BigDecimal balance, BigDecimal withdrawalAmount) {
        super("Insufficient funds. Current balance: " + balance + ", attempted withdrawal: " + withdrawalAmount + ".");
    }
}