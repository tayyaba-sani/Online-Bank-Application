package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
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
class AccountActionsServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private OperationRepository operationRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;

    private Accounts accounts;
    private Operation operation;

    private IAccountActions iAccountActions;

    @BeforeEach
    void setUp() {
        iAccountActions = new AccountActionsService(accountsRepository, operationRepository, rabbitTemplate);
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
        AccountDetails actualAccountDetails = iAccountActions.debit(accountNumber, amount);
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
        when(accountsRepository.findByAccountNumber(any(Long.class))).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountActions.debit(accountNumber, amount);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(any(Long.class));
    }

    @Test
    @DisplayName("Throw InsufficientBalanceException with given account number")
    void failedPathForDebitThrowInsufficientBalance() {
        long accountNumber = 1598472658;
        double amount = 700;
        when(accountsRepository.findByAccountNumber(any(Long.class))).thenReturn(accounts);
        assertThatThrownBy(() -> {
            iAccountActions.debit(accountNumber, amount);
        }).isInstanceOf(InsufficientBalanceException.class).hasMessage("Insufficient balance");
        verify(accountsRepository, times(1)).findByAccountNumber(any(Long.class));
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
        AccountDetails actualAccountDetails = iAccountActions.credit(accountNumber, amount);
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
            iAccountActions.credit(accountNumber, amount);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }
}