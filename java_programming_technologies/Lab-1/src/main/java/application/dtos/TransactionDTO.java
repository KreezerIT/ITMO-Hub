package application.dtos;

import core.valueobjects.Money;
import core.valueobjects.TransactionType;

import java.time.Instant;
import java.util.UUID;


public class TransactionDTO {
    public final UUID id;
    public final UUID accountid;
    public final Instant date;
    public final TransactionType type;
    public final Money usedMoney;


    public TransactionDTO(UUID id, TransactionType type, Money usedMoney, Instant date, UUID accountId) {
        this.id = id;
        this.accountid = accountId;
        this.date = date;
        this.type = type;
        this.usedMoney = usedMoney;
    }
}
