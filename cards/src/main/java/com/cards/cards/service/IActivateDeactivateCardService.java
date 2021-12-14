package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;
import com.cards.cards.dtos.CardRequest;

public interface IActivateDeactivateCardService {
    CardDetails activate(CardRequest request);
    CardDetails deactivate(CardRequest request);
}
