package application.interfaces;

import application.dtos.TransactionDTO;
import core.entities.Account;
import core.exceptions.AccountNotFoundException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidOperationException;
import core.exceptions.TransactionFailedException;
import core.valueobjects.Money;
import infrastructure.presentation.viewmodels.TransactionViewModel;

import java.sql.SQLException;
import java.util.List;

public interface TransactionService {
    void deposit(Account account, Money amount) throws SQLException, InvalidOperationException, AccountNotFoundException, TransactionFailedException;

    void withdraw(Account account, Money amountAcc) throws InvalidOperationException, SQLException, InsufficientFundsException, AccountNotFoundException, TransactionFailedException;

    List<TransactionDTO> getDTOTransactionHistory(Account account);

    List<TransactionViewModel> getTransactionHistoryByViewModel(Account account);
}
