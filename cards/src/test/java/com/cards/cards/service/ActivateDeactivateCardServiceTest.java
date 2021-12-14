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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ActivateDeactivateCardServiceTest {

    @Mock
    CardsRepository cardsRepository;

    private IActivateDeactivateCardService iActivateDeactivateCardService;

    private Cards cards;

    @BeforeEach
    void setUp() {
        iActivateDeactivateCardService = new ActivateDeactivateCardService(cardsRepository);
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
    void happyPathForActivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(cards);
        when(cardsRepository.saveAndFlush(any(Cards.class))).thenReturn(cards);
        CardDetails actualCardDetails = iActivateDeactivateCardService.activate(cardRequest);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getStatus()).isEqualTo("ACTIVE");
        verify(cardsRepository, times(1)).findByCardNumber(cardRequest.getCardNumber());
        verify(cardsRepository, times(1)).saveAndFlush(any(Cards.class));
    }

    @Test
    void failedPathForActivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(null);
        assertThatThrownBy(() -> {
            iActivateDeactivateCardService.activate(cardRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository, times(1)).findByCardNumber(cardRequest.getCardNumber());
    }

    @Test
    void happyPathForDeactivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        cards.setStatus(CardStatus.INACTIVE);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(cards);
        when(cardsRepository.saveAndFlush(any(Cards.class))).thenReturn(cards);
        CardDetails actualCardDetails = iActivateDeactivateCardService.deactivate(cardRequest);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getStatus()).isEqualTo("INACTIVE");
        verify(cardsRepository, times(1)).findByCardNumber(cardRequest.getCardNumber());
        verify(cardsRepository, times(1)).saveAndFlush(any(Cards.class));
    }

    @Test
    void failedPathForDeactivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(null);
        assertThatThrownBy(() -> {
            iActivateDeactivateCardService.deactivate(cardRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository, times(1)).findByCardNumber(cardRequest.getCardNumber());
    }

}