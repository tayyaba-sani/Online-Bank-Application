package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;

public interface ICreateDeleteAccount {
    AccountDetails createAccount(AccountDetails accountDetails);
    Boolean deleteAccount(long accountNumber);
}
