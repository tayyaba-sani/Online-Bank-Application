package com.accounts.loans.controller;

import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.service.ILoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.MethodArgumentNotValidException;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LoansController.class)
class LoansControllerTest {

    @MockBean
    private ILoanService iLoanService;

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    private LoanDetails expectedLoanDetails;
    @BeforeEach
    void setUp() {
        expectedLoanDetails = new LoanDetails();
        expectedLoanDetails.setAccountNumber(1598472658);
        expectedLoanDetails.setAmount(5000);
        expectedLoanDetails.setBranchAddress("Pasing");
        expectedLoanDetails.setLoanType("HOME");
    }

    @Test
    void happyPathToApplyForLoan() throws Exception{
        when(iLoanService.applyForLoan(any(LoanDetails.class))).thenReturn(expectedLoanDetails);
        String url = "/loans/apply-for-loan";
        mockMvc.perform(post(url).
                content(mapper.writeValueAsString(expectedLoanDetails)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1598472658));
    }
    @Test
    void failedPathToApplyForLoan() throws Exception{
        String url = "/loans/apply-for-loan";
        expectedLoanDetails.setBranchAddress(null);
        mockMvc.perform(post(url).content(mapper.writeValueAsString(expectedLoanDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));
    }
    @Test
    void happyPathToPaidLoanAmount() throws Exception {
        expectedLoanDetails.setAmountPaid(5000);
        when(iLoanService.paidLoanAmount(any(LoanDetails.class))).thenReturn(expectedLoanDetails);
        String url = "/loans/paid-loan-amount";
        mockMvc.perform(post(url).
                content(mapper.writeValueAsString(expectedLoanDetails)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.accountNumber").value(1598472658))
                .andExpect(MockMvcResultMatchers.jsonPath("$.amountPaid").value(5000));
    }
    @Test
    void failedPathToPaidLoanAmount() throws Exception {
        expectedLoanDetails.setAmount(0);
        String url = "/loans/paid-loan-amount";
        mockMvc.perform(post(url).content(mapper.writeValueAsString(expectedLoanDetails))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

    @Test
    void happyPathToGetLoansDetails() throws Exception {
        List<LoanDetails> loanDetailsList = new ArrayList<>();
        loanDetailsList.add(expectedLoanDetails);
        when(iLoanService.getLoanDetails(1598472658)).thenReturn(loanDetailsList);
        String url = "/loans/loan-details/1598472658";
        mockMvc.perform(get(url).
                contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]accountNumber").value(1598472658));
    }
    @Test
    void failedPathToGetLoansDetails() throws Exception {
        String url = "/loans/loan-details/15984728";
        mockMvc.perform(get(url)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConstraintViolationException));
    }
    @Test
    void happyPathToDeleteLoan() throws Exception {
        DeleteLoanRequest deleteLoanRequest = new DeleteLoanRequest();
        deleteLoanRequest.setAccountNumber(1598472658);
        deleteLoanRequest.setLoanType("HOME");
        when(iLoanService.deleteLoan(any(DeleteLoanRequest.class))).thenReturn("Loan details successfully deleted");
        String url = "/loans/delete-loan";
       mockMvc.perform(delete(url).
                content(mapper.writeValueAsString(deleteLoanRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value("Loan details successfully deleted"));

    }
    @Test
    void failedPathToDeleteLoan() throws Exception {
        DeleteLoanRequest deleteLoanRequest = new DeleteLoanRequest();
        deleteLoanRequest.setAccountNumber(1598472658987L);
        deleteLoanRequest.setLoanType("HOME");
        String url = "/loans/delete-loan";
        mockMvc.perform(delete(url).
                content(mapper.writeValueAsString(deleteLoanRequest)).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof MethodArgumentNotValidException));

    }

}