package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.AccountDetails;

public interface IAccountDetails {
    AccountDetails getAccountDetails(long accountNumber);
    AccountCardLoanDetails getAccountDetailsWithLoanCards(long accountNumber);
}
