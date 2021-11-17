package com.cards.cards.service;

import com.cards.cards.dtos.CardRequest;
import com.cards.cards.dtos.CardDetails;

import java.util.List;

public interface ICardService {
    CardDetails issueCard(CardDetails cardDetails);
    CardDetails activate(CardRequest request);
    CardDetails deactivate(CardRequest request);
    List<CardDetails> fetchAllCardDetails(long accountNumber);
    CardDetails fetchCardDetailsByCardNumber(long cardNumber);
    CardDetails extendExpiredDate(CardRequest request);

}
