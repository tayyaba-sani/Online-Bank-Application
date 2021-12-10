package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.dtos.*;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.ExceptionMessages;
import com.accounts.accounts.exceptions.InsufficientBalanceException;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.mappers.OperationMapper;
import com.accounts.accounts.model.*;
import com.accounts.accounts.repository.AccountsRepository;
import com.accounts.accounts.repository.OperationRepository;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Transactional
public class AccountsService implements IAccountService {

    private static final Logger logger = LoggerFactory.getLogger(AccountsService.class);
    private final AccountsRepository accountsRepository;
    private final OperationRepository operationRepository;
    private final LoansClient loansClient;
    private final CardsClient cardsClient;
    @Value("${queue.rabbitmq.exchange}")
    String exchange;
    @Value("${queue.rabbitmq.routingkey}")
    private String routingKey;
    private final RabbitTemplate rabbitTemplate;


    public AccountsService(AccountsRepository accountsRepository, OperationRepository operationRepository,
                           LoansClient loansClient, CardsClient cardsClient, RabbitTemplate rabbitTemplate) {
        this.accountsRepository = accountsRepository;
        this.operationRepository = operationRepository;
        this.loansClient = loansClient;
        this.cardsClient = cardsClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public String deleteAccount(long accountNumber) {
        logger.info("AccountsService: deleteAccount: Request: " + accountNumber);
        Accounts deleteAccountDetails = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(deleteAccountDetails)) {
            accountsRepository.delete(deleteAccountDetails);
            logger.info("AccountsService: deleteAccount: Response: " + "Account successfully deleted");
            return "Account successfully deleted";
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountDetails createAccount(AccountDetails accountDetails) {
        logger.info("AccountsService: createAccount: Request: " + accountDetails.toString());
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(generateAccountNumber());
        accounts.setAccountType(checkAccountType(accountDetails.getAccountType().toUpperCase()));
        accounts.setActualBalance(accountDetails.getBalance());
        accounts.setBranchAddress(accountDetails.getBranchAddress());
        accounts.setStatus(AccountStatus.ACTIVE);
        Customer customer = new Customer();
        customer.setAddress(accountDetails.getAddress());
        customer.setContactNumber(accountDetails.getContactNumber());
        customer.setEmail(accountDetails.getEmail());
        customer.setName(accountDetails.getName());
        accounts.setCustomer(customer);
        Operation operation = new Operation();
        operation.setAmount(accountDetails.getBalance());
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setTypeOperation(OperationType.CREDIT);
        accounts.doOperation(operation);
        accounts.setOperations(accounts.getOperations());
        Accounts newAccount = accountsRepository.saveAndFlush(accounts);
        AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(newAccount);
        rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
        logger.info("AccountsService: createAccount: Response: " + newAccountDetails.toString());
        return newAccountDetails;
    }

    @Override
    public AccountDetails getAccountDetails(long accountNumber) {
        logger.info("AccountsService: getAccountDetails: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            logger.info("AccountsService: getAccountDetails: Response: " + newAccountDetails.toString());
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountDetails debit(long accountNumber, double amount) {
        logger.info("AccountsService: debit: Request: " + accountNumber + "and" + amount);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            if (accounts.getActualBalance() < amount) {
                throw new InsufficientBalanceException(ExceptionMessages.INSUFFICIENT_BALANCE.toString());
            }
            Operation operation = new Operation();
            operation.setTypeOperation(OperationType.DEBIT);
            operation.setOperationId(UUID.randomUUID().toString());
            operation.setAmount(amount);
            accounts.setActualBalance(accounts.getActualBalance() - amount);
            operation.setAccount(accounts);
            Accounts newAccount = accountsRepository.saveAndFlush(accounts);
            operationRepository.saveAndFlush(operation);
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
            logger.info("AccountsService: debit: Response: " + newAccountDetails.toString());
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountDetails credit(long accountNumber, double amount) {
        logger.info("AccountsService: credit: Request: " + accountNumber + "and" + amount);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            Operation operation = new Operation();
            operation.setTypeOperation(OperationType.CREDIT);
            operation.setOperationId(UUID.randomUUID().toString());
            operation.setAmount(amount);
            accounts.setActualBalance(accounts.getActualBalance() + amount);
            operation.setAccount(accounts);
            Accounts newAccount = accountsRepository.saveAndFlush(accounts);
            operationRepository.saveAndFlush(operation);
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            rabbitTemplate.convertAndSend(exchange, routingKey, newAccount);
            logger.info("AccountsService: credit: Response: " + newAccountDetails.toString());
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public AccountDetails showBalance(long accountNumber) {
        logger.info("AccountsService: showBalance: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            AccountDetails newAccountDetails = AccountMapper.INSTANCE.entityToDto(accounts);
            logger.info("AccountsService: showBalance: Response: " + accountNumber);
            return newAccountDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    public List<Transaction> showTransactions(long accountNumber) {
        logger.info("AccountsService: showTransactions: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            List<Operation> operationList = accounts.getOperations();
            List<Transaction> transactionList = OperationMapper.INSTANCE.entityListToDtoList(operationList);
            logger.info("AccountsService: showTransactions: Response: " + transactionList.toString());
            return transactionList;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }

    @Override
    @Retry(name = "retryForAccountDetailsWithLoanCards", fallbackMethod = "accountDetailsWithLoanCardsFallBack")
    public AccountCardLoanDetails getAccountDetailsWithLoanCards(long accountNumber) {
        logger.info("AccountsService: getAccountDetailsWithLoanCards: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        if (!Objects.isNull(accounts)) {
            List<LoanDetails> loanDetailsList = loansClient.getLoansDetails(accountNumber);
            List<CardDetails> cardDetailsList = cardsClient.fetchAllCardDetails(accountNumber);
            AccountCardLoanDetails accountCardLoanDetails = new AccountCardLoanDetails();
            accountCardLoanDetails.setAccounts(AccountMapper.INSTANCE.entityToDto(accounts));
            accountCardLoanDetails.setCards(cardDetailsList);
            accountCardLoanDetails.setLoans(loanDetailsList);
            logger.info("AccountsService: getAccountDetailsWithLoanCards: Response: " + accountCardLoanDetails);
            return accountCardLoanDetails;
        }
        throw new AccountNotFoundException(ExceptionMessages.ACCOUNT_NOT_FOUND.toString());
    }
    private AccountCardLoanDetails accountDetailsWithLoanCardsFallBack(long accountNumber, Throwable t) {
        logger.info("AccountsService: accountDetailsWithLoanCardsFallBack: Request: " + accountNumber);
        Accounts accounts = accountsRepository.findByAccountNumber(accountNumber);
        AccountCardLoanDetails accountCardLoanDetails = new AccountCardLoanDetails();
        List<LoanDetails> loanDetailsList = null;
        List<CardDetails> cardDetailsList = null;
        if (!Objects.isNull(accounts)) {
            if (t.getCause().getMessage().equals("cards")) {
                loanDetailsList = loansClient.getLoansDetails(accountNumber);

            } else if (t.getCause().getMessage().equals("loans")) {
                cardDetailsList = cardsClient.fetchAllCardDetails(accountNumber);
            }
                accountCardLoanDetails.setAccounts(AccountMapper.INSTANCE.entityToDto(accounts));
                accountCardLoanDetails.setCards(cardDetailsList);
                accountCardLoanDetails.setLoans(loanDetailsList);
        }
        logger.info("AccountsService: accountDetailsWithLoanCardsFallBack: Response: " + accountCardLoanDetails);
        return accountCardLoanDetails;
    }

    private long generateAccountNumber() {
        return (long) Math.floor(Math.random() * 9000000000L);
    }

    private AccountType checkAccountType(String accountType) {
        if (AccountType.SAVING.name().equals(accountType)) {
            return AccountType.SAVING;
        } else {
            return AccountType.STANDING;
        }
    }
}
