package com.accounts.loans.exceptions;

public enum ExceptionMessages {

    CARD_EXIST("Card already exist"),
    NOT_FOUND_BY_ACCOUNT("Loan details not found by given account number");

    private final String value;

    ExceptionMessages(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
