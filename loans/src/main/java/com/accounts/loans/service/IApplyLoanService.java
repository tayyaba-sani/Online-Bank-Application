package com.accounts.loans.service;

import com.accounts.loans.dtos.LoanDetails;

public interface IApplyLoanService {
    LoanDetails applyForLoan(LoanDetails loanDetails);
}
