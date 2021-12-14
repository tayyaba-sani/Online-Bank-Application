package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.CardDetails;
import com.accounts.accounts.dtos.LoanDetails;
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
class AccountDetailsServiceTest {

    @Mock
    private AccountsRepository accountsRepository;
    @Mock
    private LoanClientService loansClient;
    @Mock
    private CardClientService cardsClient;

    private Accounts accounts;
    private Operation operation;

    private IAccountDetails iAccountDetails;

    @BeforeEach
    void setUp() {
        iAccountDetails = new AccountDetailsService(accountsRepository, cardsClient,loansClient);
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
    void happyPathToGetAccountDetails() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(accounts);
        AccountDetails actualAccountDetails = iAccountDetails.getAccountDetails(accountNumber);
        assertThat(actualAccountDetails).isNotNull();
        assertThat(actualAccountDetails.getAccountNumber()).isEqualTo(accountNumber);
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void failedPathToGetAccountDetails() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountDetails.getAccountDetails(accountNumber);
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
        when(loansClient.getLoanDetails(accountNumber)).thenReturn(loanDetailsList);
        when(cardsClient.getCardDetails(accountNumber)).thenReturn(cardDetailsList);
        AccountCardLoanDetails actual = iAccountDetails.getAccountDetailsWithLoanCards(accountNumber);
        assertThat(actual).isNotNull();
        assertThat(actual.getLoans()).isNotNull();
        assertThat(actual.getCards()).isNotNull();
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
        verify(loansClient, times(1)).getLoanDetails(accountNumber);
        verify(cardsClient, times(1)).getCardDetails(accountNumber);

    }

    @Test
    @DisplayName("Throw AccountNotFoundException with given account number")
    void failedPathToGetAccountDetailsWithLoanCards() {
        long accountNumber = 1598472658;
        when(accountsRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iAccountDetails.getAccountDetailsWithLoanCards(accountNumber);
        }).isInstanceOf(AccountNotFoundException.class).hasMessage("Given account number does not exist");
        verify(accountsRepository, times(1)).findByAccountNumber(accountNumber);
    }
}