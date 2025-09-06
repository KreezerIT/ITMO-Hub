package application.interfaces;

import application.dtos.AccountDTO;
import core.exceptions.*;
import core.valueobjects.Money;
import infrastructure.presentation.viewmodels.TransactionViewModel;

import java.sql.SQLException;
import java.util.List;

public interface AccountService {
    void createAccount(String accountNumber, String pinCode, String currency) throws IllegalArgumentException, DatabaseOperationException;

    String getBalance(String accountNumber) throws SQLException, InvalidAccountException;

    void deposit(String accountNumber, Money inputedMoney) throws SQLException, InvalidOperationException, InvalidAccountException, AccountNotFoundException, TransactionFailedException;

    void withdraw(String accountNumber, double moneyToWithdraw) throws SQLException, InvalidAccountException, InvalidOperationException, InsufficientFundsException, AccountNotFoundException, TransactionFailedException;

    AccountDTO getDTOAccountDetails(String accountNumber) throws InvalidAccountException, SQLException;

    List<TransactionViewModel> getAccountTransactionsByViewModel(String accountNumber) throws SQLException, InvalidAccountException;

    boolean checkByAccountNumber(String accountNumber) throws SQLException, DataMappingException;

    boolean verifyPin(String accountNumber, String pin) throws SQLException, InvalidPinCodeException, AccountNotFoundException;
}
