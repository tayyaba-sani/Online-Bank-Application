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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteLoanServiceTest {
    @Mock
    private LoansRepository loansRepository;

    private IDeleteLoanService iDeleteLoanService;

    private Loans loans;
    private LoanDetails expectedLoanDetails;

    @BeforeEach
    void setUp() {
        iDeleteLoanService = new DeleteLoanService(loansRepository);
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
    void happyPathToDeleteLoan() {
        DeleteLoanRequest deleteLoanRequest = new DeleteLoanRequest();
        deleteLoanRequest.setAccountNumber(1598472658);
        deleteLoanRequest.setLoanType("HOME");
        when(loansRepository.findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME))
                .thenReturn(loans);
        boolean actual = iDeleteLoanService.deleteLoan(deleteLoanRequest);
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
            iDeleteLoanService.deleteLoan(deleteLoanRequest);
        }).isInstanceOf(LoanDetailsNotFoundException.class).hasMessage("Loan details not found by given account number");
        verify(loansRepository, times(1)).findByAccountNumberAndLoanType(expectedLoanDetails.getAccountNumber(), LoanType.HOME);

    }
}