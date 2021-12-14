package com.accounts.loans.service;

import com.accounts.loans.dtos.LoanDetails;

public interface IPaidLoanService {
    LoanDetails paidLoanAmount(LoanDetails loanDetails);
}
