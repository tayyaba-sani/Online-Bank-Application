package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.TransactionResponse;
import com.accounts.accounts.exceptions.AccountNotFoundException;
import com.accounts.accounts.model.*;
import com.accounts.accounts.repository.AccountsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionRequestDetailsServiceTest {

    @Mock
    private AccountsRepository accountsRepository;

    private Accounts accounts;
    private Operation operation;

    private ITransactionDetails iTransactionDetails;

    @BeforeEach
    void setUp() {
        iTransactionDetails = new TransactionDetailsService(accountsRepository);
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
    void happyPathToShowBalance() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        AccountDetails actualAccountDetails = iTransactionDetails.showBalance(accountNumber);
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
            iTransactionDetails.showBalance(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToShowTransactions() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        List<TransactionResponse> transactionList = iTransactionDetails.showTransactions(accountNumber);
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
            iTransactionDetails.showTransactions(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }
}