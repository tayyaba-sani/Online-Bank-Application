package com.cards.cards.service;

import com.cards.cards.dtos.CardDetails;
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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardDetailServiceTest {

    @Mock
    CardsRepository cardsRepository;

    private ICardDetailService iCardDetailService;

    private Cards cards;

    @BeforeEach
    void setUp() {
        iCardDetailService = new CardDetailService(cardsRepository);
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
    void happyPathToFetchAllCardDetails() {
        long accountNumber = 1598472658;
        List<Cards> cardsList = new ArrayList<>();
        cardsList.add(cards);
        when(cardsRepository.findByAccountNumber(accountNumber)).thenReturn(cardsList);
        List<CardDetails> actualCardsList = iCardDetailService.fetchAllCardDetails(accountNumber);
        assertThat(actualCardsList).isNotNull();
        assertThat(actualCardsList.size()).isEqualTo(1);
        verify(cardsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void failedPathToFetchAllCardDetails() {
        long accountNumber = 1598472658;
        List<Cards> cardsList = new ArrayList<>();
        when(cardsRepository.findByAccountNumber(accountNumber)).thenReturn(cardsList);
        assertThatThrownBy(() -> {
            iCardDetailService.fetchAllCardDetails(accountNumber);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository, times(1)).findByAccountNumber(accountNumber);
    }

    @Test
    void happyPathToFetchCardDetailsByCardNumber() {
        long cardNumber = 58426791;
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(cards);
        CardDetails actualCardsDetails = iCardDetailService.fetchCardDetailsByCardNumber(cardNumber);
        assertThat(actualCardsDetails).isNotNull();
        assertThat(actualCardsDetails.getCardType()).isEqualTo(cards.getCardType().name());
        verify(cardsRepository, times(1)).findByCardNumber(cardNumber);
    }

    @Test
    void failedPathToFetchCardDetailsByCardNumber() {
        long cardNumber = 58426791;
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iCardDetailService.fetchCardDetailsByCardNumber(cardNumber);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository, times(1)).findByCardNumber(cardNumber);
    }
}