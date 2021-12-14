package com.accounts.loans.service;

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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaidLoanServiceTest {

    @Mock
    private LoansRepository loansRepository;

    private IPaidLoanService iPaidLoanService;

    private Loans loans;
    private LoanDetails expectedLoanDetails;

    @BeforeEach
    void setUp() {
        iPaidLoanService = new PaidLoanService(loansRepository);
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
    void happyPathToPaidLoanAmount() {

        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(loans);
        when(loansRepository.saveAndFlush(any(Loans.class))).thenReturn(loans);
        LoanDetails actual = iPaidLoanService.paidLoanAmount(expectedLoanDetails);
        assertThat(actual).isNotNull();
        assertThat(actual.getAccountNumber()).isEqualTo(expectedLoanDetails.getAccountNumber());
        assertThat(actual.getTotalLoan()).isEqualTo(500);
        verify(loansRepository, times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME);
        verify(loansRepository, times(1)).saveAndFlush(any(Loans.class));
    }

    @Test
    void failedPathToPaidLoanAmount() {
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(null);
        assertThatThrownBy(() -> {
            iPaidLoanService.paidLoanAmount(expectedLoanDetails);
        }).isInstanceOf(LoanDetailsNotFoundException.class).hasMessage("Loan details not found by given account number");
        verify(loansRepository, times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME);

    }
}