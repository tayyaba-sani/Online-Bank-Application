package com.accounts.loans.service;


import com.accounts.loans.dtos.LoanDetails;

import java.util.List;

public interface ILoanDetailService {
    List<LoanDetails> getLoanDetails(long accountNumber);
}
