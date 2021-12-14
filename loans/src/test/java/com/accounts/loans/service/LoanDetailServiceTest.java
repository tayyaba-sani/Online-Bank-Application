package com.accounts.loans.service;

import com.accounts.loans.config.LoansServiceConfig;
import com.accounts.loans.dtos.LoanDetails;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanDetailServiceTest {
    @Mock
    private LoansRepository loansRepository;

    private ILoanDetailService iLoanDetailService;


    private Loans loans;
    private LoanDetails expectedLoanDetails;

    @BeforeEach
    void setUp() {
        iLoanDetailService = new LoanDetailService(loansRepository);
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
    void happyPathToGetLoanDetails() {
        List<Loans> loansList = new ArrayList<>();
        loansList.add(loans);
        when(loansRepository.findByAccountNumber(1598472658)).thenReturn(loansList);
        List<LoanDetails> actual = iLoanDetailService.getLoanDetails(1598472658);
        assertThat(actual.isEmpty()).isFalse();
        assertThat(actual.size()).isEqualTo(1);
        verify(loansRepository, times(1)).findByAccountNumber(1598472658);
    }

    @Test
    void failedPathToGetLoanDetails() {
        when(loansRepository.findByAccountNumber(1598472658)).thenReturn(new ArrayList<>());
        List<LoanDetails> actual = iLoanDetailService.getLoanDetails(1598472658);
        assertThat(actual.isEmpty()).isTrue();
        assertThat(actual.size()).isEqualTo(0);
        verify(loansRepository, times(1)).findByAccountNumber(1598472658);
    }
}