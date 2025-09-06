package infrastructure.services;

import application.dtos.TransactionDTO;
import core.entities.Transaction;
import core.exceptions.DataMappingException;
import core.exceptions.TransactionFailedException;
import core.repositories.TransactionRepository;
import infrastructure.interfaces.Logger;

import java.util.Collection;
import java.util.UUID;

public class TransactionRepositoryWithLoggingImpl implements TransactionRepository {
    private final TransactionRepository innerRepository;
    private final Logger logger;

    public TransactionRepositoryWithLoggingImpl(TransactionRepository innerRepository, Logger logger) {
        this.innerRepository = innerRepository;
        this.logger = logger;
    }

    public void save(TransactionDTO transaction) throws TransactionFailedException {
        logger.log("Saving transaction with Id: " + transaction.id + ", AccountId: " + transaction.accountid);
        innerRepository.save(transaction);
        logger.log("Transaction with Id: " + transaction.id + " saved successfully.");
    }

    public Collection<Transaction> getByAccountId(UUID accountId) throws DataMappingException {
        logger.log("Fetching transactions for AccountId: " + accountId);
        Collection<Transaction> transactions = innerRepository.getByAccountId(accountId);
        logger.log("Fetched " + transactions.size() + " transactions for AccountId: " + accountId);
        return transactions;
    }
}