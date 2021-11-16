package com.accounts.accounts.dtos;

import io.swagger.annotations.ApiModel;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;


@ApiModel(description = "Details about the account and customer")
public class AccountDetails {

    @NotNull
    private String accountType;
    private long accountNumber;
    @NotNull
    private String branchAddress;
    private String status;
    @NotNull
    private double balance;

    @NotNull
    private String name;
    @NotNull
    private String address;
    @NotNull
    private int contactNumber;
    @NotNull
    @Email(message = "Email should be valid", regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    private String email;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(int contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBranchAddress() {
        return branchAddress;
    }

    public void setBranchAddress(String branchAddress) {
        this.branchAddress = branchAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountDetails{" +
                "accountType='" + accountType + '\'' +
                ", accountNumber=" + accountNumber +
                ", branchAddress='" + branchAddress + '\'' +
                ", status='" + status + '\'' +
                ", balance=" + balance +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", contactNumber=" + contactNumber +
                ", email='" + email + '\'' +
                '}';
    }
}
