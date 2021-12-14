package com.accounts.loans.service;

import com.accounts.loans.dtos.DeleteLoanRequest;

public interface IDeleteLoanService {
    Boolean deleteLoan(DeleteLoanRequest request);
}
