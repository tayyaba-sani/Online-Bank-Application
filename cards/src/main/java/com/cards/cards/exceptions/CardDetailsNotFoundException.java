package com.cards.cards.exceptions;


public class CardDetailsNotFoundException extends RuntimeException {
    public CardDetailsNotFoundException(String message)
    {
        super(message);
    }
}
