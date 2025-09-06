package core.repositories;

import application.dtos.TransactionDTO;
import core.entities.Transaction;
import core.exceptions.DataMappingException;
import core.exceptions.TransactionFailedException;

import java.util.Collection;
import java.util.UUID;

public interface TransactionRepository {
    void save(TransactionDTO transaction) throws TransactionFailedException;

    Collection<Transaction> getByAccountId(UUID accountId) throws DataMappingException;
}