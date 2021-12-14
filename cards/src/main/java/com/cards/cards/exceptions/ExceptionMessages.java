package com.cards.cards.exceptions;

public enum ExceptionMessages {

    CARD_EXIST("Card already exist"),
    NOT_FOUND_BY_ACCOUNT("Card details not found by given account number"),
    NOT_FOUND_BY_CARD("Card details not found by given card number");

    private final String value;

    ExceptionMessages(final String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
