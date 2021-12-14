package com.accounts.accounts.dtos;

import lombok.ToString;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ToString
public class TransactionRequest {

    @NotNull
    @Min(value = 1, message = "Amount should be greater than 0")
    private double amount;
    @NotNull
    @Range(min = 1000000000L, max = 9999999999L)
    private long accountNumber;

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }
}
