package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.model.*;
import com.accounts.accounts.repository.AccountsRepository;
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
class CreateDeleteAccountServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private RabbitTemplate rabbitTemplate;

    private Accounts accounts;
    private Operation operation;

    private ICreateDeleteAccount iCreateDeleteAccount;

    @BeforeEach
    void setUp() {
        iCreateDeleteAccount = new CreateDeleteAccountService(accountsRepository, rabbitTemplate);
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
        expectedAccountDetails.setStatus(AccountStatus.ACTIVE.name());
        when(accountsRepository.saveAndFlush(any(Accounts.class))).thenReturn(accounts);
        AccountDetails actualAccountDetails = iCreateDeleteAccount.createAccount(expectedAccountDetails);
        expectedAccountDetails.setAccountNumber(1598472658);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getAccountNumber()).isEqualTo(expectedAccountDetails.getAccountNumber());
        verify(accountsRepository, times(1)).saveAndFlush(ArgumentMatchers.any(Accounts.class));

    }

    @Test
    @DisplayName("Delete account with given account number")
    void happyPathForDeleteAccount() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        boolean actual = iCreateDeleteAccount.deleteAccount(accountNumber);
        assertThat(actual).isTrue();
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(accountsRepository, times(1)).delete(ArgumentMatchers.any(Accounts.class));
    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathForDeleteAccount() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iCreateDeleteAccount.deleteAccount(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }
}