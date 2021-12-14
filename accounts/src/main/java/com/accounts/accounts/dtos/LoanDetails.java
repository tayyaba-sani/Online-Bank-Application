package com.accounts.accounts.dtos;


import lombok.ToString;

@ToString
public class LoanDetails {
    private long accountNumber;

    private int amount;

    private String branchAddress;

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
