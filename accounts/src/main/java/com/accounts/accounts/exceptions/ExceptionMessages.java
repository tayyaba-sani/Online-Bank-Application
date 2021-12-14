package com.accounts.accounts.exceptions;

public enum ExceptionMessages {

    ACCOUNT_NOT_FOUND("Given account number does not exist"),
    INSUFFICIENT_BALANCE("Insufficient balance"),
    INVALID_CARD_NUMBER("Invalid card number");


    private final String value;

    ExceptionMessages(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
