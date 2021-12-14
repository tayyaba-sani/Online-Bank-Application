package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;

import java.util.List;

public interface ICardDetailService {
    List<CardDetails> fetchAllCardDetails(long accountNumber);
    CardDetails fetchCardDetailsByCardNumber(long cardNumber);
}
