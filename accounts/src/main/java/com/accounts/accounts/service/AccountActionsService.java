package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.ExceptionMessages;
import com.accounts.accounts.exceptions.InsufficientBalanceException;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.mappers.OperationMapper;
import com.accounts.accounts.model.Accounts;
import com.accounts.accounts.model.Operation;
import com.accounts.accounts.model.OperationType;
import com.accounts.accounts.repository.AccountsRepository;
import com.accounts.accounts.repository.OperationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;

@Service
@Transactional
public class AccountActionsService implements IAccountActions {
    private static final Logger logger = LoggerFactory.getLogger(AccountActionsService.class);
    private final AccountsRepository accountsRepository;
    private final OperationRepository operationRepository;

    @Value("${queue.rabbitmq.exchange}")
    String exchange;
    @Value("${queue.rabbitmq.routingkey}")
    String routingKey;
    private final RabbitTemplate rabbitTemplate;

    public AccountActionsService(AccountsRepository accountsRepository, OperationRepository operationRepository, RabbitTemplate rabbitTemplate) {
        this.accountsRepository = accountsRepository;
        this.operationRepository = operationRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public AccountDetails debit(long accountNumber, double amount) {
        logger.info("AccountActionsService: debit: Request: " + accountNumber + "and" + amount);
            Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
            if (!Objects.isNull(accounts)) {
                if (accounts.getActualBalance() < amount) {
                    throw new InsufficientBalanceException(ExceptionMessages.INSUFFICIENT_BALANCE.toString());
                }
                Operation operation = OperationMapper.INSTANCE.DTOToEntity(accounts,amount,accounts.getActualBalance() - amount,OperationType.DEBIT);
                Accounts newAccount = accountsRepository.saveAndFlush(accounts);
                operationRepository.saveAndFlush(operation);
                AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
                rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
                logger.info("AccountActionsService: debit: Response: " + newAccountDetails.toString());
                return newAccountDetails;
            }
            throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountDetails credit(long accountNumber, double amount) {
        logger.info("AccountActionsService: credit: Request: " + accountNumber + "and" + amount);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            Operation operation = OperationMapper.INSTANCE.DTOToEntity(accounts,amount,accounts.getActualBalance() + amount, OperationType.CREDIT);
            Accounts newAccount = accountsRepository.saveAndFlush(accounts);
            operationRepository.saveAndFlush(operation);
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
            logger.info("AccountActionsService: credit: Response: " + newAccountDetails.toString());
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }
}
