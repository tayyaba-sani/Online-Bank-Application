package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.dtos.CardRequest;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.exceptions.ExceptionMessages;
import com.cards.cards.mappers.CardMapper;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ActivateDeactivateCardService implements IActivateDeactivateCardService{
    private static final Logger logger = LoggerFactory.getLogger(ActivateDeactivateCardService.class);

    private final CardsRepository cardsRepository;

    public ActivateDeactivateCardService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @Override
    public CardDetails activate(CardRequest request) {
        logger.info("ActivateDeactivateCardService: activate : Request: "+request.toString());
        CardDetails newCardDetails = setActivateOrDeactivate(CardStatus.ACTIVE, request.getCardNumber());
        logger.info("ActivateDeactivateCardService: activate : Response: "+newCardDetails.toString());
        return newCardDetails;
    }

    @Override
    public CardDetails deactivate(CardRequest request) {
        logger.info("ActivateDeactivateCardService: deactivate : Request: "+request.toString());
        CardDetails newCardDetails = setActivateOrDeactivate(CardStatus.INACTIVE, request.getCardNumber());
        logger.info("ActivateDeactivateCardService: deactivate : Response: "+newCardDetails.toString());
        return newCardDetails;
    }
    private CardDetails setActivateOrDeactivate(CardStatus cardStatus, long cardNumber) {
        Cards cards = cardsRepository.findByCardNumber(cardNumber);
        if (!Objects.isNull(cards)) {
            cards.setStatus(cardStatus);
            cardsRepository.saveAndFlush(cards);
            return CardMapper.INSTANCE.entityToDto(cards);
        }
        throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_CARD.toString());
    }
}
