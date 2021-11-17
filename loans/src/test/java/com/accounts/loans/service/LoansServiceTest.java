package com.accounts.loans.service;

import com.accounts.loans.config.LoansServiceConfig;
import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.exceptions.LoanDetailsNotFoundException;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoansServiceTest {

    @Mock
    private LoansRepository loansRepository;

    private ILoanService iLoanService;

    @Mock
    private LoansServiceConfig loansServiceConfig;

    private Loans loans;
    private LoanDetails expectedLoanDetails;
    @BeforeEach
    void setUp() {
        iLoanService = new LoansService(loansRepository,loansServiceConfig);
        loans = new Loans();
        loans.setAccountNumber(1598472658);
        loans.setAmountPaid(0);
        loans.setBranchAddress("Pasing");
        loans.setLoanType(LoanType.HOME);
        loans.setAmount(5000);
        loans.setTotalLoan(5500);

        expectedLoanDetails = new LoanDetails();
        expectedLoanDetails.setAccountNumber(1598472658);
        expectedLoanDetails.setAmount(5000);
        expectedLoanDetails.setBranchAddress("Pasing");
        expectedLoanDetails.setLoanType("HOME");
    }
    @Test
    void happyPathToApplyForLoan() {

        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(loans);
        when(loansRepository.saveAndFlush(any(Loans.class))).thenReturn(loans);
        when(loansServiceConfig.getInterestRate()).thenReturn(10);
        LoanDetails actual = iLoanService.applyForLoan(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(5000+(5000*10)/100 + 5500);
        verify(loansRepository,times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(),LoanType.HOME);
        verify(loansRepository,times(1)).saveAndFlush(any(Loans.class));
    }
    @Test
    void happyPathToApplyForLoanForFirstTime() {
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(null);
        when(loansRepository.saveAndFlush(any(Loans.class))).thenReturn(loans);
        when(loansServiceConfig.getInterestRate()).thenReturn(10);
        LoanDetails actual = iLoanService.applyForLoan(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(5000+(5000*10)/100);
        verify(loansRepository,times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(),LoanType.HOME);
        verify(loansRepository,times(1)).saveAndFlush(any(Loans.class));
    }

    @Test
    void happyPathToPaidLoanAmount() {

        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(loans);
        when(loansRepository.saveAndFlush(any(Loans.class))).thenReturn(loans);
        LoanDetails actual = iLoanService.paidLoanAmount(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(500);
        verify(loansRepository,times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(),LoanType.HOME);
        verify(loansRepository,times(1)).saveAndFlush(any(Loans.class));
    }
    @Test
    void failedPathToPaidLoanAmount() {
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(null);
        assertThatThrownBy(() -> {
            iLoanService.paidLoanAmount(expectedLoanDetails);
        }).isInstanceOf(LoanDetailsNotFoundException.class).hasMessage("Loan detail not found with given account number");
         verify(loansRepository,times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(),LoanType.HOME);

    }
    @Test
    void happyPathToDeleteLoan() {
        DeleteLoanRequest deleteLoanRequest = new DeleteLoanRequest();
        deleteLoanRequest.setAccountNumber(1598472658);
        deleteLoanRequest.setLoanType("HOME");
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(loans);
        String actual = iLoanService.deleteLoan(deleteLoanRequest);
        assertThat(actual).isEqualTo("Loan details successfully deleted");

    }
    @Test
    void failedPathToDeleteLoan() {
        DeleteLoanRequest deleteLoanRequest = new DeleteLoanRequest();
        deleteLoanRequest.setAccountNumber(1598472658);
        deleteLoanRequest.setLoanType("HOME");
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(null);
        assertThatThrownBy(() -> {
            iLoanService.deleteLoan(deleteLoanRequest);
        }).isInstanceOf(LoanDetailsNotFoundException.class).hasMessage("Loan detail not found with given account number");
        verify(loansRepository,times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(),LoanType.HOME);

    }
    @Test
    void happyPathToGetLoanDetails() {
        List<Loans> loansList = new ArrayList<>();
        loansList.add(loans);
        when(loansRepository.findByAccountNumber(1598472658)).thenReturn(loansList);
        List<LoanDetails> actual = iLoanService.getLoanDetails(1598472658);
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.size()).isEqualTo(1);
        verify(loansRepository,times(1)).findByAccountNumber(1598472658);
    }
    @Test
    void failedPathToGetLoanDetails() {
        when(loansRepository.findByAccountNumber(1598472658)).thenReturn(new ArrayList<>());
        List<LoanDetails> actual = iLoanService.getLoanDetails(1598472658);
        assertThat(actual.isEmpty()).isTrue();
        assertThat(actual.size()).isEqualTo(0);
        verify(loansRepository,times(1)).findByAccountNumber(1598472658);
    }
}