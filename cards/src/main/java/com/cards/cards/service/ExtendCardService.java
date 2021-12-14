package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.dtos.CardRequest;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.exceptions.ExceptionMessages;
import com.cards.cards.mappers.CardMapper;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;

@Service
public class ExtendCardService implements IExtendCardService {
    private static final Logger logger = LoggerFactory.getLogger(ExtendCardService.class);

    private final CardsRepository cardsRepository;

    public ExtendCardService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @Override
    public CardDetails extendExpiredDate(CardRequest request) {
        logger.info("ExtendCardService: extendExpiredDate : Request: "+request.toString());
        Cards cards = cardsRepository.findByCardNumber(request.getCardNumber());
        if (!Objects.isNull(cards)) {
            cards.setExpiredDate(LocalDateTime.now().plusYears(4));
            CardDetails newCardDetails = CardMapper.INSTANCE.entityToDto(cards);
            logger.info("ExtendCardService: extendExpiredDate : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_CARD.toString());
    }
}
