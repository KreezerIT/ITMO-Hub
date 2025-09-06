package application.services;

import application.dtos.AccountDTO;
import application.interfaces.AccountService;
import core.entities.Account;
import core.exceptions.*;
import core.valueobjects.CurrencyType;
import core.valueobjects.Money;
import infrastructure.persistance.AccountRepositoryImpl;
import infrastructure.presentation.viewmodels.TransactionViewModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class AccountServiceImpl implements AccountService {
    private final AccountRepositoryImpl repository;
    private final TransactionServiceImpl transactionServiceImpl;

    public AccountServiceImpl(AccountRepositoryImpl repository, TransactionServiceImpl transactionServiceImpl) {
        this.repository = repository;
        this.transactionServiceImpl = transactionServiceImpl;
    }

    public void createAccount(String accountNumber, String pinCode, String currency) throws IllegalArgumentException, DatabaseOperationException {
        CurrencyType currencyType;
        try {
            currencyType = CurrencyType.valueOf(currency);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid currency type: " + currency);
        }
        Account account = new Account(accountNumber, pinCode, currencyType);
        repository.save(account);
    }

    public String getBalance(String accountNumber) throws SQLException, InvalidAccountException {
        AccountDTO account = getDTOAccountDetails(accountNumber);
        if (account == null) {
            throw new InvalidAccountException(accountNumber);
        }
        return account.balance.toString();
    }

    public void deposit(String accountNumber, Money inputMoney) throws SQLException, InvalidOperationException, InvalidAccountException, AccountNotFoundException, TransactionFailedException {
        Account account = repository.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new InvalidAccountException(accountNumber);
        }
        transactionServiceImpl.deposit(account, inputMoney);
    }

    public void withdraw(String accountNumber, double amount) throws SQLException, InvalidAccountException, InvalidOperationException, InsufficientFundsException, AccountNotFoundException, TransactionFailedException {
        Account account = repository.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new InvalidAccountException(accountNumber);
        }
        transactionServiceImpl.withdraw(account, new Money(new BigDecimal(amount), account.balance.getCurrency()));
    }

    public AccountDTO getDTOAccountDetails(String accountNumber) throws InvalidAccountException, SQLException {
        Account account = repository.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new InvalidAccountException(accountNumber);
        }
        return new AccountDTO(account.id, accountNumber, account.balance, transactionServiceImpl.getDTOTransactionHistory(account), account.pinCode);
    }

    public boolean verifyPin(String accountNumber, String pin) throws SQLException, InvalidPinCodeException, AccountNotFoundException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        if (pin == null || pin.isEmpty()) {
            throw new IllegalArgumentException("Pin cannot be null or empty.");
        }

        return repository.verifyAccountPin(accountNumber, pin);
    }

    public boolean checkByAccountNumber(String accountNumber) throws DataMappingException {
        if (accountNumber == null || accountNumber.isEmpty()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }

        return repository.checkAccountExistenceByNumber(accountNumber);
    }

    public List<TransactionViewModel> getAccountTransactionsByViewModel(String accountNumber) throws SQLException, InvalidAccountException {
        Account account = repository.getAccountByNumber(accountNumber);
        if (account == null) {
            throw new InvalidAccountException(accountNumber);
        }
        return transactionServiceImpl.getTransactionHistoryByViewModel(account);
    }
}
