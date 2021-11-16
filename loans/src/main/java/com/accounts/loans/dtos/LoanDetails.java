package com.accounts.loans.dtos;


import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@ToString
public class LoanDetails {
    @NotNull
    @Range(min = 1000000000L, max = 9999999999L)
    private long accountNumber;
    @NotNull
    @Min(value = 1, message = "Amount should be greater than 0")
    private int amount;
    @NotNull
    private String branchAddress;
    @NotNull
    private String loanType;

    private long LoanNumber;
    private long totalLoan;
    private long amountPaid;

    public long getLoanNumber() {
        return LoanNumber;
    }

    public void setLoanNumber(long loanNumber) {
        LoanNumber = loanNumber;
    }

    public long getTotalLoan() {
        return totalLoan;
    }

    public void setTotalLoan(long totalLoan) {
        this.totalLoan = totalLoan;
    }

    public long getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(long amountPaid) {
        this.amountPaid = amountPaid;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getLoanType() {
        return loanType;
    }

    public void setLoanType(String loanType) {
        this.loanType = loanType;
    }
}
