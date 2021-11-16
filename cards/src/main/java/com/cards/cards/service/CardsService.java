package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardRequest;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.exceptions.CardExistException;
import com.cards.cards.exceptions.ExceptionMessages;
import com.cards.cards.mappers.CardMapper;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.CardType;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class CardsService implements ICardService {

    private static final Logger logger = LoggerFactory.getLogger(CardsService.class);

    private final CardsRepository cardsRepository;
    private final CardsServiceConfig cardsServiceConfig;

    public CardsService(CardsRepository cardsRepository, CardsServiceConfig cardsServiceConfig) {
        this.cardsRepository = cardsRepository;
        this.cardsServiceConfig = cardsServiceConfig;
    }

    @Override
    public CardDetails issueCard(CardDetails cardDetails) {
        logger.info("CardsService: issueCard : Request: "+cardDetails.toString());
        Cards OldCardDetails = cardsRepository.findByAccountNumberAndCardType(cardDetails.getAccountNumber(),
                checkCardType(cardDetails.getCardType().toUpperCase()));
        if(Objects.isNull(OldCardDetails)) {
            Cards cards = new Cards();
            cards.setStatus(CardStatus.ACTIVE);
            cards.setAccountNumber(cardDetails.getAccountNumber());
            cards.setBranchAddress(cardDetails.getBranchAddress());
            cards.setPersonalAddress(cardDetails.getPersonalAddress());
            cards.setCardType(checkCardType(cardDetails.getCardType().toUpperCase()));
            cards.setCardNumber(generateCardNumber());
            cards.setDailyLimit(cardsServiceConfig.getDailyLimit());
            cards.setExpiredDate(LocalDateTime.now().plusYears(4));
            Cards newCard = cardsRepository.saveAndFlush(cards);
            CardDetails newCardDetails = CardMapper.INSTANCE.entityToDto(newCard);
            logger.info("CardsService: issueCard : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardExistException(ExceptionMessages.CARD_EXIST.toString());
    }

    @Override
    public CardDetails activate(CardRequest request) {
        logger.info("CardsService: activate : Request: "+request.toString());
        CardDetails newCardDetails = setActivateOrDeactivate(CardStatus.ACTIVE, request.getCardNumber());
        logger.info("CardsService: activate : Response: "+newCardDetails.toString());
        return newCardDetails;
    }

    @Override
    public CardDetails deactivate(CardRequest request) {
        logger.info("CardsService: deactivate : Request: "+request.toString());
        CardDetails newCardDetails = setActivateOrDeactivate(CardStatus.INACTIVE, request.getCardNumber());
        logger.info("CardsService: deactivate : Response: "+newCardDetails.toString());
        return newCardDetails;
    }

    @Override
    public List<CardDetails> fetchAllCardDetails(long accountNumber) {
        logger.info("CardsService: fetchAllCardDetails : Request: "+accountNumber);
        List<Cards> cardsList = cardsRepository.findByAccountNumber(accountNumber);
        if (cardsList.isEmpty()) {
            throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_ACCOUNT.toString());
        }
        List<CardDetails> cardDetailsList = CardMapper.INSTANCE.entityListToDtoList(cardsList);
        logger.info("CardsService: fetchAllCardDetails : Response: "+cardDetailsList.toString());
        return cardDetailsList;
    }

    @Override
    public CardDetails fetchCardDetailsByCardNumber(long cardNumber) {
        logger.info("CardsService: fetchCardDetailsByCardNumber : Request: "+cardNumber);
        Cards cards = cardsRepository.findByCardNumber(cardNumber);
        if (!Objects.isNull(cards)) {
            CardDetails newCardDetails = CardMapper.INSTANCE.entityToDto(cards);
            logger.info("CardsService: fetchCardDetailsByCardNumber : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_CARD.toString());
    }
    @Override
    public CardDetails extendExpiredDate(CardRequest request) {
        logger.info("CardsService: extendExpiredDate : Request: "+request.toString());
        Cards cards = cardsRepository.findByCardNumber(request.getCardNumber());
        if (!Objects.isNull(cards)) {
            cards.setExpiredDate(LocalDateTime.now().plusYears(4));
            CardDetails newCardDetails = CardMapper.INSTANCE.entityToDto(cards);
            logger.info("CardsService: extendExpiredDate : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_CARD.toString());
    }
    private CardType checkCardType(String cardType) {
        if (cardType.equals(CardType.CREDIT.name())) {
            return CardType.CREDIT;
        } else {
            return CardType.DEBIT;
        }
    }

    private long generateCardNumber() {
        return (long) Math.floor(Math.random() * 9000000000L);
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
