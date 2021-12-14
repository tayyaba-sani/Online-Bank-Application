package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.ExceptionMessages;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.model.*;
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
public class CreateDeleteAccountService implements ICreateDeleteAccount{
    private static final Logger logger = LoggerFactory.getLogger(CreateDeleteAccountService.class);
    private final AccountsRepository accountsRepository;
    @Value("${queue.rabbitmq.exchange}")
    String exchange;
    @Value("${queue.rabbitmq.routingkey}")
    String routingKey;
    private final RabbitTemplate rabbitTemplate;

    public CreateDeleteAccountService(AccountsRepository accountsRepository, RabbitTemplate rabbitTemplate) {
        this.accountsRepository = accountsRepository;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public AccountDetails createAccount(AccountDetails accountDetails) {
        logger.info("CreateDeleteAccountService: createAccount: Request: " + accountDetails.toString());
        accountDetails.setAccountNumber(generateAccountNumber());
        Accounts newAccount = accountsRepository.saveAndFlush(AccountMapper.INSTANCE.DtoToEntity(accountDetails));
        AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(newAccount);
        rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
        logger.info("CreateDeleteAccountService: createAccount: Response: " + newAccountDetails.toString());
        return newAccountDetails;
    }

    @Override
    public Boolean deleteAccount(long accountNumber) {
        logger.info("CreateDeleteAccountService: deleteAccount: Request: " + accountNumber);
        Accounts deleteAccountDetails = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(deleteAccountDetails)) {
            accountsRepository.delete(deleteAccountDetails);
            logger.info("CreateDeleteAccountService: deleteAccount: Response: " + "Account successfully deleted");
            return true;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    private long generateAccountNumber() {
        return (long) Math.floor(Math.random() * 9000000000L);
    }
}
