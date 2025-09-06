package application.dtos;

import core.valueobjects.Money;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class AccountDTO {
    public final UUID id;
    public final Money balance;
    public final List<TransactionDTO> transactions;
    public final String accountNumber;
    public final String pinCode;


    public AccountDTO(UUID id, String accountNumber, Money balance, List<TransactionDTO> dtoTransactionHistory, String pinCode) {
        this.id = id;
        this.balance = balance;
        this.transactions = new ArrayList<>(dtoTransactionHistory);
        this.accountNumber = accountNumber;
        this.pinCode = pinCode;
    }
}
