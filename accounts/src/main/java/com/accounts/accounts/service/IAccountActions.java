package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;

public interface IAccountActions {
    AccountDetails debit(long cardNumber, double amount);
    AccountDetails credit(long accountNumber, double amount);
}
