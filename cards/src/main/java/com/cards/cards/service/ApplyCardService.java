package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardDetails;
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
import java.util.Objects;

@Service
public class ApplyCardService implements IApplyCardService {
    private static final Logger logger = LoggerFactory.getLogger(ApplyCardService.class);

    private final CardsRepository cardsRepository;
    private final CardsServiceConfig cardsServiceConfig;

    public ApplyCardService(CardsRepository cardsRepository, CardsServiceConfig cardsServiceConfig) {
        this.cardsRepository = cardsRepository;
        this.cardsServiceConfig = cardsServiceConfig;
    }

    @Override
    public CardDetails issueCard(CardDetails cardDetails) {
        logger.info("ApplyCardService: issueCard : Request: "+cardDetails.toString());
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
            logger.info("ApplyCardService: issueCard : Response: "+newCardDetails.toString());
            return newCardDetails;
        }
        throw new CardExistException(ExceptionMessages.CARD_EXIST.toString());
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
}
