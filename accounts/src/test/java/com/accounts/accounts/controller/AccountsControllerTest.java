package com.accounts.accounts.controller;

import com.accounts.accounts.dtos.*;
import com.accounts.accounts.service.IAccountActions;
import com.accounts.accounts.service.IAccountDetails;
import com.accounts.accounts.service.ICreateDeleteAccount;
import com.accounts.accounts.service.ITransactionDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AccountsController.class)
class AccountsControllerTest {


    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IAccountDetails iAccountDetails;
    @MockBean
    private IAccountActions iAccountActions;
    @MockBean
    private ICreateDeleteAccount iCreateDeleteAccount;
    @MockBean
    private ITransactionDetails iTransactionDetails;


    private AccountDetails accountDetails;
    private AccountCardLoanDetails accountCardLoanDetails;
    private long accountNumber;

    @BeforeEach
    void setUp() {
        accountDetails = new AccountDetails();
        accountDetails.setAccountType("SAVING_ACCOUNT");
        accountDetails.setAddress("Planegger");
        accountDetails.setBalance(500);
        accountDetails.setContactNumber(5458);
        accountDetails.setEmail("tayyaba.sani@yahoo.com");
        accountDetails.setName("Tayyaba");
        accountDetails.setBranchAddress("Pasing str");
        accountNumber = 1598472658;
    }

    @Test
    void happyPathToCreateAccount() throws Exception {

        accountDetails.setAccountNumber(accountNumber);
        when(iCreateDeleteAccount.createAccount(accountDetails)).thenReturn(accountDetails);
        String url = "/account/create-account";
        mockMvc.perform(post(url).
                content(mapper.writeValueAsString(accountDetails)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void failedPathForCreateAccount() throws Exception {

        accountDetails.setBranchAddress(null);
        String url = "/account/create-account";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(accountDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void happyPathToDeleteAccount() throws Exception {
        when(iCreateDeleteAccount.deleteAccount(accountNumber)).thenReturn(true);
        String url = "/account/delete-account/1598472658";
        mockMvc.perform(delete(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(true));

    }

    @Test
    void happyPathToGetAccountDetails() throws Exception {

        when(iAccountDetails.getAccountDetails(accountNumber)).thenReturn(accountDetails);
        String url = "/account/account-details/1598472658";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tayyaba"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(500));
    }

    @Test
    void happyPathToGetAccountDetailsWithLoanCards() throws Exception {

        setAccountDetailsWithLoanCards();
        when(iAccountDetails.getAccountDetailsWithLoanCards(accountNumber)).thenReturn(accountCardLoanDetails);
        String url = "/account/account-details-with-loans-cards/1598472658";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accounts.accountNumber").value("1598472658"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.loans[0].loanType").value("CAR"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.cards[0].cardType").value("DEBIT"));

    }

    @Test
    void happyPathForDebit() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAccountNumber(1598472658);
        transactionRequest.setAmount(50);
        accountDetails.setBalance(450);
        when(iAccountActions.debit(transactionRequest.getAccountNumber(), transactionRequest.getAmount())).thenReturn(accountDetails);
        String url = "/account/debit";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(450));
    }

    @Test
    void failedPathForDebit() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        accountDetails.setBalance(450);
        String url = "/account/debit";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void happyPathForCredit() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        transactionRequest.setAccountNumber(accountNumber);
        transactionRequest.setAmount(100);
        accountDetails.setBalance(600);
        when(iAccountActions.credit(transactionRequest.getAccountNumber(), transactionRequest.getAmount())).thenReturn(accountDetails);
        String url = "/account/credit";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(600));
    }

    @Test
    void failedPathForCredit() throws Exception {
        TransactionRequest transactionRequest = new TransactionRequest();
        String url = "/account/credit";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(transactionRequest))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void happyPathToShowBalance() throws Exception {

        when(iTransactionDetails.showBalance(accountNumber)).thenReturn(accountDetails);
        String url = "/account/show-balance/1598472658";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tayyaba"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(500));
    }

    @Test
    void happyPathToShowTransactions() throws Exception {

        List<TransactionResponse> transactionList = new ArrayList<>();
        TransactionResponse transaction = new TransactionResponse();
        transaction.setTransactionId("2");
        transaction.setCreatedDate(LocalDateTime.now().minusDays(2));
        transaction.setOperationType("DEBIT");
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(20);
        transactionList.add(transaction);
        transaction = new TransactionResponse();
        transaction.setTransactionId("3");
        transaction.setCreatedDate(LocalDateTime.now().minusDays(1));
        transaction.setOperationType("CREDIT");
        transaction.setAccountNumber(accountNumber);
        transaction.setAmount(20);
        transactionList.add(transaction);
        when(iTransactionDetails.showTransactions(accountNumber)).thenReturn(transactionList);
        String url = "/account/show-transactions/1598472658";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].operationType").value("DEBIT"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].operationType").value("CREDIT"));
    }

    private void setAccountDetailsWithLoanCards() {
        accountCardLoanDetails = new AccountCardLoanDetails();
        accountDetails.setAccountNumber(1598472658);
        accountCardLoanDetails.setAccounts(accountDetails);
        List<CardDetails> cardDetailsList = new ArrayList<>();
        CardDetails cardDetails = new CardDetails();
        cardDetails.setAccountNumber(accountNumber);
        cardDetails.setCardType("DEBIT");
        cardDetails.setBranchAddress("Pasing");
        cardDetails.setDailyLimit(500);
        cardDetails.setPersonalAddress("Planegger");
        cardDetails.setCardNumber(555588446);
        cardDetails.setStatus("ACTIVE");
        cardDetails.setExpiredDate(LocalDateTime.now());
        cardDetailsList.add(cardDetails);
        accountCardLoanDetails.setCards(cardDetailsList);
        List<LoanDetails> loanDetailsList = new ArrayList<>();
        LoanDetails loanDetails = new LoanDetails();
        loanDetails.setAccountNumber(accountNumber);
        loanDetails.setAmountPaid(10);
        loanDetails.setAmount(100);
        loanDetails.setBranchAddress("Pasing");
        loanDetails.setLoanType("CAR");
        loanDetails.setLoanNumber(598742635);
        loanDetails.setTotalLoan(500);
        loanDetailsList.add(loanDetails);
        accountCardLoanDetails.setLoans(loanDetailsList);
    }
}