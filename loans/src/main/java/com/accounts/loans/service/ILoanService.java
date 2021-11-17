package com.accounts.loans.service;

import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.dtos.LoanDetails;

import java.util.List;

public interface ILoanService {
    LoanDetails applyForLoan(LoanDetails loanDetails);
    LoanDetails paidLoanAmount(LoanDetails loanDetails);
    String deleteLoan(DeleteLoanRequest request);
    List<LoanDetails> getLoanDetails(long account);
}
