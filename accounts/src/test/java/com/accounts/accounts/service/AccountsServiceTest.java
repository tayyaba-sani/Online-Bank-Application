package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.config.RabbitMQConfig;
import com.accounts.accounts.dtos.*;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.exceptions.InsufficientBalanceException;
import com.accounts.accounts.model.*;
import com.accounts.accounts.repository.AccountsRepository;
import com.accounts.accounts.repository.OperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountsServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private OperationRepository operationRepository;

    @Mock
    private LoansClient loansClient;
    @Mock
    private CardsClient cardsClient;
    @Mock
    private RabbitTemplate rabbitTemplate;

    private Accounts accounts;
    private Operation operation;

    private IAccountService iAccountService;

    @BeforeEach
    void setUp() {
        iAccountService = new AccountsService(accountsRepository, operationRepository, loansClient, cardsClient,rabbitTemplate);
        accounts = new Accounts();
        accounts.setAccountNumber(1598472658);
        accounts.setAccountType(AccountType.SAVING);
        accounts.setActualBalance(500);
        accounts.setBranchAddress("Pasing str");
        accounts.setStatus(AccountStatus.ACTIVE);
        Customer customer = new Customer();
        customer.setAddress("Planegger");
        customer.setContactNumber(4587156);
        customer.setEmail("tayyaba.sani@yahoo.com");
        customer.setName("Tayyaba Sani");
        accounts.setCustomer(customer);
        operation = new Operation();
        operation.setAmount(500);
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setTypeOperation(OperationType.CREDIT);
        accounts.doOperation(operation);
        List<Operation> operationList = new ArrayList<>();
        operationList.add(operation);
    }

    @Test
    @DisplayName("Create account")
    void HappyPathTestForCreateAccount() {

        AccountDetails expectedAccountDetails = new AccountDetails();
        expectedAccountDetails.setAccountType("SAVING");
        expectedAccountDetails.setAddress("Planegger");
        expectedAccountDetails.setBalance(500);
        expectedAccountDetails.setContactNumber(5458);
        expectedAccountDetails.setEmail("tayyaba.sani@yahoo.com");
        expectedAccountDetails.setName("Tayyaba");
        expectedAccountDetails.setBranchAddress("Pasing str");
        expectedAccountDetails.setAccountNumber(1598472658);
        expectedAccountDetails.setStatus(AccountStatus.ACTIVE.name());
        when(accountsRepository.saveAndFlush(any(Accounts.class))).thenReturn(accounts);
        AccountDetails actualAccountDetails = iAccountService.createAccount(expectedAccountDetails);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getAccountNumber()).isEqualTo(expectedAccountDetails.getAccountNumber());
        verify(accountsRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Accounts.class));

    }

    @Test
    @DisplayName("Delete account with given account number")
    void happyPathForDeleteAccount() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        String actualMsg = iAccountService.deleteAccount(accountNumber);
        assertThat(actualMsg).isNotEmpty();
        assertThat(actualMsg).isEqualTo("Account successfully deleted");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountsRepository, times(1)).delete(ArgumentMatchers.any(Accounts.class));
    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathForDeleteAccount() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.deleteAccount(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToGetAccountDetails() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        AccountDetails actualAccountDetails = iAccountService.getAccountDetails(accountNumber);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getAccountNumber()).isEqualTo(accountNumber);
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void failedPathToGetAccountDetails() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.getAccountDetails(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathForDebit() {
        long accountNumber = 1598472658;
        double amount = 200;
        AccountDetails expectedAccountDetails = new AccountDetails();
        expectedAccountDetails.setAccountType("SAVING_ACCOUNT");
        expectedAccountDetails.setAddress("Planegger");
        expectedAccountDetails.setBalance(300);
        expectedAccountDetails.setContactNumber(5458);
        expectedAccountDetails.setEmail("tayyaba.sani@yahoo.com");
        expectedAccountDetails.setName("Tayyaba");
        expectedAccountDetails.setBranchAddress("Pasing str");
        expectedAccountDetails.setAccountNumber(1598472658);
        expectedAccountDetails.setAccountType(AccountType.SAVING.name());
        expectedAccountDetails.setStatus(AccountStatus.ACTIVE.name());

        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        when(accountsRepository.saveAndFlush(any(Accounts.class))).thenReturn(accounts);
        when(operationRepository.saveAndFlush(any(Operation.class))).thenReturn(operation);
        AccountDetails actualAccountDetails = iAccountService.debit(accountNumber, amount);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getBalance()).isEqualTo(expectedAccountDetails.getBalance());
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountsRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Accounts.class));
        verify(operationRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Operation.class));

    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathForDebit() {
        long accountNumber = 1598472658;
        double amount = 200;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.debit(accountNumber, amount);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Throw InsufficientBalanceException with given account number")
    void failedPathForDebitThrowInsufficientBalance() {
        long accountNumber = 1598472658;
        double amount = 700;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        assertThatThrownBy(() -> {
            iAccountService.debit(accountNumber, amount);
        }).isInstanceOf(InsufficientBalanceException.class).hasMessage("Insufficient balance");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathForCredit() {
        long accountNumber = 1598472658;
        double amount = 200;
        AccountDetails expectedAccountDetails = new AccountDetails();
        expectedAccountDetails.setAccountType("SAVING_ACCOUNT");
        expectedAccountDetails.setAddress("Planegger");
        expectedAccountDetails.setBalance(700);
        expectedAccountDetails.setContactNumber(5458);
        expectedAccountDetails.setEmail("tayyaba.sani@yahoo.com");
        expectedAccountDetails.setName("Tayyaba");
        expectedAccountDetails.setBranchAddress("Pasing str");
        expectedAccountDetails.setAccountNumber(1598472658);
        expectedAccountDetails.setAccountType(AccountType.SAVING.name());
        expectedAccountDetails.setStatus(AccountStatus.ACTIVE.name());

        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        when(accountsRepository.saveAndFlush(any(Accounts.class))).thenReturn(accounts);
        when(operationRepository.saveAndFlush(any(Operation.class))).thenReturn(operation);
        AccountDetails actualAccountDetails = iAccountService.credit(accountNumber, amount);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getBalance()).isEqualTo(expectedAccountDetails.getBalance());
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountsRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Accounts.class));
        verify(operationRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Operation.class));
    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathForCredit() {
        long accountNumber = 1598472658;
        double amount = 200;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.credit(accountNumber, amount);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToShowBalance() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        AccountDetails actualAccountDetails = iAccountService.showBalance(accountNumber);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getAccountNumber()).isEqualTo(accountNumber);
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathToShowBalance() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.showBalance(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToShowTransactions() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        List<Transaction> transactionList = iAccountService.showTransactions(accountNumber);
        assertThat(transactionList).isNotNull();
        assertThat(transactionList.size()).isEqualTo(1);
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathToShowTransactions() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.showTransactions(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToGetAccountDetailsWithLoanCards() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        List<LoanDetails> loanDetailsList = new ArrayList<>();
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAccountNumber(1598472658);
        loanDetails.setAmount(200);
        loanDetails.setAmountPaid(100);
        loanDetails.setBranchAddress("Pasing");
        loanDetails.setLoanType("Personal");
        loanDetails.setTotalLoan(200);
        loanDetailsList.add(loanDetails);
        List<CardDetails> cardDetailsList = new ArrayList<>();
        CardDetails cardDetails = new CardDetails();
        cardDetails.setAccountNumber(1598472658);
        cardDetails.setBranchAddress("Pasing");
        cardDetails.setCardNumber(558842559);
        cardDetails.setDailyLimit(50);
        cardDetails.setPersonalAddress("Planeger");
        cardDetails.setStatus("ACTIVE");
        cardDetailsList.add(cardDetails);
        when(loansClient.getLoansDetails(accountNumber)).thenReturn(loanDetailsList);
        when(cardsClient.fetchAllCardDetails(accountNumber)).thenReturn(cardDetailsList);
        AccountCardLoanDetails actual = iAccountService.getAccountDetailsWithLoanCards(accountNumber);
        assertThat(actual).isNotNull();
        assertThat(actual.getLoans()).isNotNull();
        assertThat(actual.getCards()).isNotNull();
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(loansClient, times(1)).getLoansDetails(accountNumber);
        verify(cardsClient, times(1)).fetchAllCardDetails(accountNumber);

    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathToGetAccountDetailsWithLoanCards() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountService.getAccountDetailsWithLoanCards(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }
}