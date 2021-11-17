package com.cards.cards.exceptions;

public class CardExistException extends RuntimeException {
    public CardExistException(String message)
    {
        super(message);
    }
}
