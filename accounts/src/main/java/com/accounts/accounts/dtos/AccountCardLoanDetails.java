package com.accounts.accounts.dtos;

import com.accounts.accounts.model.Accounts;
import java.util.List;

public class AccountCardLoanDetails {
    private AccountDetails accounts;
    private List<LoanDetails> loans;
    private List<CardDetails> cards;

    public AccountDetails getAccounts() {
        return accounts;
    }

    public void setAccounts(AccountDetails accounts) {
        this.accounts = accounts;
    }

    public List<LoanDetails> getLoans() {
        return loans;
    }

    public void setLoans(List<LoanDetails> loans) {
        this.loans = loans;
    }

    public List<CardDetails> getCards() {
        return cards;
    }

    public void setCards(List<CardDetails> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return "AccountCardLoanDetails{" +
                "accounts=" + accounts +
                ", loans=" + loans +
                ", cards=" + cards +
                '}';
    }
}
