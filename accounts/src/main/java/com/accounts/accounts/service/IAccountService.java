package com.accounts.accounts.service;

import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.Transaction;

import java.util.List;

public interface IAccountService {
    AccountDetails createAccount(AccountDetails accountDetails);
    String deleteAccount(long accountNumber);
    AccountDetails getAccountDetails(long accountNumber);
    AccountDetails debit(long accountNumber, double amount);
    AccountDetails credit(long accountNumber, double amount);
    AccountDetails showBalance(long accountNumber);
    List<Transaction> showTransactions(long accountNumber);
    AccountCardLoanDetails getAccountDetailsWithLoanCards(long accountNumber);
}
