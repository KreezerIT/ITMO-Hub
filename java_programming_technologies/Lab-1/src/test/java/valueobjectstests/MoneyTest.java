package valueobjectstests;

import core.exceptions.WrongCurrencyException;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class MoneyTest {

    @Test
    void shouldCreateMoneySuccessfully() {
        Money money = new Money(new BigDecimal("100.00"), CurrencyType.USD);
        assertEquals(new BigDecimal("100.00"), money.getAmount());
        assertEquals(CurrencyType.USD, money.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenCurrencyIsNull() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Money(new BigDecimal("100.00"), null)
        );
        assertEquals("Currency cannot be null or empty.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenAmountIsNegative() {
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                new Money(new BigDecimal("-10.00"), CurrencyType.USD)
        );
        assertEquals("Amount cannot be negative.", exception.getMessage());
    }

    @Test
    void shouldAddMoneyCorrectly() {
        Money money1 = new Money(new BigDecimal("50.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("30.00"), CurrencyType.USD);
        Money result = money1.add(money2);

        assertEquals(new BigDecimal("80.00"), result.getAmount());
        assertEquals(CurrencyType.USD, result.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenAddingDifferentCurrencies() {
        Money money1 = new Money(new BigDecimal("50.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("30.00"), CurrencyType.EUR);

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                money1.add(money2)
        );
        assertEquals("Currencies must be the same for addition.", exception.getMessage());
    }

    @Test
    void shouldSubtractMoneyCorrectly() throws WrongCurrencyException {
        Money money1 = new Money(new BigDecimal("100.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("40.00"), CurrencyType.USD);
        Money result = money1.subtract(money2);

        assertEquals(new BigDecimal("60.00"), result.getAmount());
        assertEquals(CurrencyType.USD, result.getCurrency());
    }

    @Test
    void shouldThrowExceptionWhenSubtractingMoreThanAvailable() {
        Money money1 = new Money(new BigDecimal("30.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("50.00"), CurrencyType.USD);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                money1.subtract(money2)
        );
        assertEquals("Insufficient funds for this operation.", exception.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenSubtractingDifferentCurrencies() {
        Money money1 = new Money(new BigDecimal("50.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("30.00"), CurrencyType.EUR);

        Exception exception = assertThrows(WrongCurrencyException.class, () ->
                money1.subtract(money2)
        );
        assertEquals("The currency '" + CurrencyType.EUR + "' is not equals '" + CurrencyType.USD + "'", exception.getMessage());
    }

    @Test
    void shouldBeEqualIfSameAmountAndCurrency() {
        Money money1 = new Money(new BigDecimal("100.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("100.00"), CurrencyType.USD);

        assertEquals(money1, money2);
        assertEquals(money1.hashCode(), money2.hashCode());
    }

    @Test
    void shouldNotBeEqualIfDifferentAmount() {
        Money money1 = new Money(new BigDecimal("100.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("90.00"), CurrencyType.USD);

        assertNotEquals(money1, money2);
    }

    @Test
    void shouldNotBeEqualIfDifferentCurrency() {
        Money money1 = new Money(new BigDecimal("100.00"), CurrencyType.USD);
        Money money2 = new Money(new BigDecimal("100.00"), CurrencyType.EUR);

        assertNotEquals(money1, money2);
    }

    @Test
    void shouldReturnCorrectStringRepresentation() {
        Money money = new Money(new BigDecimal("250.75"), CurrencyType.USD);
        assertEquals("250,75 USD", money.toString());
    }
}
