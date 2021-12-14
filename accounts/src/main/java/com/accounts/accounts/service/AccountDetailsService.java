package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.CardDetails;
import com.accounts.accounts.dtos.LoanDetails;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.ExceptionMessages;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.model.Accounts;
import com.accounts.accounts.repository.AccountsRepository;
import com.accounts.accounts.repository.OperationRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AccountDetailsService implements IAccountDetails {

    private static final Logger logger = LoggerFactory.getLogger(AccountDetailsService.class);
    private final AccountsRepository accountsRepository;
    private final CardClientService cardClientService;
    private final LoanClientService loanClientService;

    public AccountDetailsService(AccountsRepository accountsRepository, CardClientService cardClientService, LoanClientService loanClientService) {
        this.accountsRepository = accountsRepository;
        this.cardClientService = cardClientService;
        this.loanClientService = loanClientService;
    }

    @Override
    public AccountDetails getAccountDetails(long accountNumber) {
        logger.info("AccountDetailsService: getAccountDetails: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            logger.info("AccountDetailsService: getAccountDetails: Response: " + newAccountDetails.toString());
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountCardLoanDetails getAccountDetailsWithLoanCards(long accountNumber) {
        logger.info("AccountDetailsService: getAccountDetailsWithLoanCards: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            List<LoanDetails> loanDetailsList = loanClientService.getLoanDetails(accountNumber);
            List<CardDetails> cardDetailsList = cardClientService.getCardDetails(accountNumber);
            AccountCardLoanDetails accountCardLoanDetails = new AccountCardLoanDetails();
            accountCardLoanDetails.setAccounts(AccountMapper.INSTANCE.entityToDto(accounts));
            accountCardLoanDetails.setCards(cardDetailsList);
            accountCardLoanDetails.setLoans(loanDetailsList);
            logger.info("AccountDetailsService: getAccountDetailsWithLoanCards: Response: " + accountCardLoanDetails);
            return accountCardLoanDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }
}
