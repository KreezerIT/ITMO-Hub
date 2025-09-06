package infrastructure.presentation.viewmodels;

import core.valueobjects.Money;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel {
    private String accountNumber;
    private Money balance;
    private List<TransactionViewModel> transactions;

    public AccountViewModel() {
        this.transactions = new ArrayList<>();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Money getBalance() {
        return balance;
    }

    public void setBalance(Money balance) {
        this.balance = balance;
    }

    public List<TransactionViewModel> getTransactions() {
        return transactions;
    }

    @Override
    public String toString() {
        return "Счет: " + accountNumber + "\n" +
                "Баланс: " + (balance != null ? balance.getAmount() + " " + balance.getCurrency() : "") + "\n" +
                "Количество транзакций: " + transactions.size();
    }
}
