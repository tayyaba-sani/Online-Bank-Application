package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;
import com.cards.cards.dtos.CardRequest;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.CardType;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExtendCardServiceTest {

    @Mock
    CardsRepository cardsRepository;

    private IExtendCardService iExtendCardService;

    private Cards cards;

    @BeforeEach
    void setUp() {
        iExtendCardService = new ExtendCardService(cardsRepository);
        cards = new Cards();
        cards.setAccountNumber(1598472658);
        cards.setBranchAddress("Pasing");
        cards.setCardNumber(485967555);
        cards.setCardType(CardType.DEBIT);
        cards.setDailyLimit(3);
        cards.setExpiredDate(LocalDateTime.now().plusYears(5));
        cards.setPersonalAddress("Planegger");
        cards.setStatus(CardStatus.ACTIVE);
    }

    @Test
    void happyPathToExtendExpiredDate() {
        long cardNumber = 58426791;
        CardRequest cardExpiredDateRequest = new CardRequest();
        cardExpiredDateRequest.setCardNumber(cardNumber);
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(cards);
        CardDetails actualCardsDetails = iExtendCardService.extendExpiredDate(cardExpiredDateRequest);
        assertThat(actualCardsDetails).isNotNull();
        assertThat(actualCardsDetails.getExpiredDate()).isEqualToIgnoringMinutes(LocalDateTime.now().plusYears(4));
        verify(cardsRepository, times(1)).findByCardNumber(cardNumber);
    }

    @Test
    void failedPathToExtendExpiredDate() {
        long cardNumber = 58426791;
        CardRequest cardExpiredDateRequest = new CardRequest();
        cardExpiredDateRequest.setCardNumber(cardNumber);
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iExtendCardService.extendExpiredDate(cardExpiredDateRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository, times(1)).findByCardNumber(cardNumber);
    }
}