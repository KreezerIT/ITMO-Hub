package core.entities;

import core.valueobjects.CurrencyType;
import core.valueobjects.Money;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Account {
    public UUID id;
    public Money balance;
    private final List<Transaction> transactionHistory;
    public String accountNumber;
    public String pinCode;

    public Account(String accountNumber, String pinCode, CurrencyType currency) {
        this.id = UUID.randomUUID();
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.balance = new Money(BigDecimal.ZERO, currency);
        this.transactionHistory = new ArrayList<>();
    }

    public Account(UUID id, String accountNumber, String pinCode, Money balance, List<Transaction> transactions) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
        this.balance = balance;
        this.transactionHistory = transactions;
    }

    public void setBalance(BigDecimal balance) {
        this.balance.amount = balance;
    }

    public void addTransaction(Transaction transaction) {
        transactionHistory.add(transaction);
    }

    public List<Transaction> getTransactionHistory() {
        return Collections.unmodifiableList(transactionHistory);
    }
}