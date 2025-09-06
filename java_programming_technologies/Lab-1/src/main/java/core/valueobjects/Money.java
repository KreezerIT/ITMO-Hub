package core.valueobjects;

import core.exceptions.WrongCurrencyException;

import java.math.BigDecimal;
import java.util.Objects;

public class Money {
    public BigDecimal amount;
    private final CurrencyType currency;

    public Money(BigDecimal amount, CurrencyType currency) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Amount cannot be negative.");
        }

        if (currency == null) {
            throw new IllegalArgumentException("Currency cannot be null or empty.");
        }

        this.amount = amount;
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public CurrencyType getCurrency() {
        return currency;
    }

    public Money add(Money other) {
        if (!this.currency.equals(other.currency)) {
            throw new IllegalArgumentException("Currencies must be the same for addition.");
        }
        return new Money(this.amount.add(other.amount), this.currency);
    }

    public Money subtract(Money other) throws WrongCurrencyException {
        ensureSameCurrency(other);
        if (this.amount.compareTo(other.amount) < 0) {
            throw new IllegalStateException("Insufficient funds for this operation.");
        }
        return new Money(this.amount.subtract(other.amount), this.currency);
    }

    private void ensureSameCurrency(Money other) throws WrongCurrencyException {
        if (this.currency != other.currency) {
            throw new WrongCurrencyException(other.currency.toString(), this.currency.toString());
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Money money = (Money) obj;
        return amount.compareTo(money.amount) == 0 && Objects.equals(currency, money.currency);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, currency);
    }

    @Override
    public String toString() {
        return String.format("%.2f %s", amount, currency);
    }

    public static Money add(Money a, Money b) {
        return a.add(b);
    }

    public static Money subtract(Money a, Money b) throws WrongCurrencyException {
        return a.subtract(b);
    }
}

