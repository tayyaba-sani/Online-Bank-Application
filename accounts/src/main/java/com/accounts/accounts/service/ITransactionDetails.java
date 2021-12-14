package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.TransactionResponse;

import java.util.List;

public interface ITransactionDetails {
    AccountDetails showBalance(long accountNumber);

    List<TransactionResponse> showTransactions(long accountNumber);
}
