package com.cards.cards.dtos;

import lombok.ToString;
import org.hibernate.validator.constraints.Range;
import javax.validation.constraints.NotNull;

@ToString
public class CardRequest {
    @NotNull
    @Range(min = 1000000000L, max = 9999999999L)
    private long cardNumber;

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(long cardNumber) {
        this.cardNumber = cardNumber;
    }
}
