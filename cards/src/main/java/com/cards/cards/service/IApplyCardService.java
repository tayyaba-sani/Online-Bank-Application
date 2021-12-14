package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;

public interface IApplyCardService {
    CardDetails issueCard(CardDetails cardDetails);
}
