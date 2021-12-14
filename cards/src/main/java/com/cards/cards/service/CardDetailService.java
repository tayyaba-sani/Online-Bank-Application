package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.exceptions.ExceptionMessages;
import com.cards.cards.mappers.CardMapper;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CardDetailService implements ICardDetailService{
    private static final Logger logger = LoggerFactory.getLogger(CardDetailService.class);

    private final CardsRepository cardsRepository;

    public CardDetailService(CardsRepository cardsRepository) {
        this.cardsRepository = cardsRepository;
    }

    @Override
    public List<CardDetails> fetchAllCardDetails(long accountNumber) {
        logger.info("CardDetailService: fetchAllCardDetails : Request: "+accountNumber);
        List<Cards> cardsList = cardsRepository.findByAccountNumber(accountNumber);
        if (cardsList.isEmpty()) {
            throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_ACCOUNT.toString());
        }
        List<CardDetails> cardDetailsList = CardMapper.INSTANCE.entityListToDtoList(cardsList);
        logger.info("CardDetailService: fetchAllCardDetails : Response: "+cardDetailsList.toString());
        return cardDetailsList;
    }

    @Override
    public CardDetails fetchCardDetailsByCardNumber(long cardNumber) {
        logger.info("CardDetailService: fetchCardDetailsByCardNumber : Request: "+cardNumber);
        Cards cards = cardsRepository.findByCardNumber(cardNumber);
        if (!Objects.isNull(cards)) {
            CardDetails newCardDetails = CardMapper.INSTANCE.entityToDto(cards);
            logger.info("CardDetailService: fetchCardDetailsByCardNumber : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_CARD.toString());
    }
}
