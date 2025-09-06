package core.entities;

import core.valueobjects.Money;
import core.valueobjects.TransactionType;

import java.time.Instant;
import java.util.UUID;

public class Transaction {
    public UUID id;
    public String accountId;
    public Instant date;
    public TransactionType type;
    public Money usedMoney;

    public Transaction(TransactionType type, Money usedMoney, Instant dateTime, String accountId) {
        this.id = UUID.randomUUID();
        this.type = type;
        this.usedMoney = usedMoney;
        this.date = dateTime;
        this.accountId = accountId;
    }

    public Transaction(UUID id, TransactionType type, Money usedMoney, Instant dateTime, String accountId) {
        this.id = id;
        this.type = type;
        this.usedMoney = usedMoney;
        this.date = dateTime;
        this.accountId = accountId;
    }
}
