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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyLoanServiceTest {

    @Mock
    private LoansRepository loansRepository;

    private IApplyLoanService iApplyLoanService;

    @Mock
    private LoansServiceConfig loansServiceConfig;

    private Loans loans;
    private LoanDetails expectedLoanDetails;

    @BeforeEach
    void setUp() {
        iApplyLoanService = new ApplyLoanService(loansRepository, loansServiceConfig);
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
        LoanDetails actual = iApplyLoanService.applyForLoan(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(5000 + (5000 * 10) / 100 + 5500);
        verify(loansRepository, times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME);
        verify(loansRepository, times(1)).saveAndFlush(any(Loans.class));
    }

    @Test
    void happyPathToApplyForLoanForFirstTime() {
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(null);
        when(loansRepository.saveAndFlush(any(Loans.class))).thenReturn(loans);
        when(loansServiceConfig.getInterestRate()).thenReturn(10);
        LoanDetails actual = iApplyLoanService.applyForLoan(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(5000 + (5000 * 10) / 100);
        verify(loansRepository, times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME);
        verify(loansRepository, times(1)).saveAndFlush(any(Loans.class));
    }
}