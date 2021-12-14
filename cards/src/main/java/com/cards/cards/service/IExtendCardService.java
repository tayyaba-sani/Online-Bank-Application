package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;
import com.cards.cards.dtos.CardRequest;

public interface IExtendCardService {
    CardDetails extendExpiredDate(CardRequest request);
}
