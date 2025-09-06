package application.services;

import application.dtos.TransactionDTO;
import application.interfaces.TransactionService;
import core.entities.Account;
import core.entities.Transaction;
import core.exceptions.AccountNotFoundException;
import core.exceptions.InsufficientFundsException;
import core.exceptions.InvalidOperationException;
import core.exceptions.TransactionFailedException;
import core.valueobjects.Money;
import core.valueobjects.TransactionType;
import infrastructure.persistance.AccountRepositoryImpl;
import infrastructure.persistance.TransactionRepositoryImpl;
import infrastructure.presentation.viewmodels.TransactionViewModel;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    private final AccountRepositoryImpl accountRepositoryImpl;
    private final TransactionRepositoryImpl transactionRepositoryImpl;

    public TransactionServiceImpl(AccountRepositoryImpl accountRepositoryImpl, TransactionRepositoryImpl TransactionRepositoryImpl) {
        this.accountRepositoryImpl = accountRepositoryImpl;
        this.transactionRepositoryImpl = TransactionRepositoryImpl;
    }

    public void deposit(Account account, Money amountM) throws SQLException, InvalidOperationException, AccountNotFoundException, TransactionFailedException {
        if (!account.balance.getCurrency().equals(amountM.getCurrency())) {
            throw new InvalidOperationException("Currency mismatch.");
        }
        Transaction currentTransaction = new Transaction(TransactionType.ACCOUNT_REPLENISHMENT, amountM, Instant.now(), account.accountNumber);
        TransactionDTO currentTransactionDTO = new TransactionDTO(UUID.randomUUID(), TransactionType.ACCOUNT_REPLENISHMENT, amountM, Instant.now(), account.id);
        account.setBalance(account.balance.amount.add(amountM.amount));
        account.addTransaction(currentTransaction);
        transactionRepositoryImpl.save(currentTransactionDTO);
        accountRepositoryImpl.update(account);

    }

    public void withdraw(Account account, Money amountToWithdraw) throws InvalidOperationException, SQLException, InsufficientFundsException, AccountNotFoundException, TransactionFailedException {
        BigDecimal amountM = amountToWithdraw.amount;
        if (account.balance.getAmount().compareTo(amountM) < 0) {
            throw new InsufficientFundsException(account.balance.amount, amountToWithdraw.amount);
        }

        account.setBalance(account.balance.amount.subtract(amountM));
        account.addTransaction(new Transaction(
                TransactionType.WITHDRAWAL_FROM_ACCOUNT,
                new Money(amountM, account.balance.getCurrency()),
                Instant.now(),
                account.accountNumber
        ));

        TransactionDTO currentTransactionDTO = new TransactionDTO(UUID.randomUUID(), TransactionType.WITHDRAWAL_FROM_ACCOUNT, amountToWithdraw, Instant.now(), account.id);
        accountRepositoryImpl.update(account);
        transactionRepositoryImpl.save(currentTransactionDTO);
    }

    public List<TransactionDTO> getDTOTransactionHistory(Account account) throws IllegalArgumentException {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        return account.getTransactionHistory().stream()
                .map(t -> new TransactionDTO(
                        t.id,
                        t.type,
                        t.usedMoney,
                        t.date,
                        UUID.fromString(t.accountId)
                ))
                .collect(Collectors.toList());
    }

    public List<TransactionViewModel> getTransactionHistoryByViewModel(Account account) throws IllegalArgumentException {
        if (account == null) {
            throw new IllegalArgumentException("Account cannot be null.");
        }

        List<TransactionDTO> transactionDTOs = getDTOTransactionHistory(account);

        return transactionDTOs.stream()
                .map(TransactionViewModel::fromDTO)
                .collect(Collectors.toList());
    }
}
