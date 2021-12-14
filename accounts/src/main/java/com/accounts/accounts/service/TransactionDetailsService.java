package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.TransactionResponse;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.ExceptionMessages;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.mappers.OperationMapper;
import com.accounts.accounts.model.Accounts;
import com.accounts.accounts.model.Operation;
import com.accounts.accounts.repository.AccountsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class TransactionDetailsService implements ITransactionDetails{

    private static final Logger logger = LoggerFactory.getLogger(TransactionDetailsService.class);
    private final AccountsRepository accountsRepository;

    public TransactionDetailsService(AccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    @Override
    public AccountDetails showBalance(long accountNumber) {
        logger.info("TransactionDetailsService: showBalance: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            logger.info("TransactionDetailsService: showBalance: Response: " + accountNumber);
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public List<TransactionResponse> showTransactions(long accountNumber) {
        logger.info("TransactionDetailsService: showTransactions: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            List<Operation> operationList = accounts.getOperations();
            List<TransactionResponse> transactionList = OperationMapper.INSTANCE.entityListToDtoList(operationList);
            logger.info("TransactionDetailsService: showTransactions: Response: " + transactionList.toString());
            return transactionList;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }
}
