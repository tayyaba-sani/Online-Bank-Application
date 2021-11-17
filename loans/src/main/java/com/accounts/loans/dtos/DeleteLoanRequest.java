package com.accounts.loans.dtos;

import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@ToString
public class DeleteLoanRequest {
    @NotNull
    @Range(min = 1000000000L, max = 9999999999L)
    private long accountNumber;
    @NotNull
    private String loanType;

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
