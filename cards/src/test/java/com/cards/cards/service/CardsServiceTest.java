package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardRequest;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.exceptions.CardDetailsNotFoundException;
import com.cards.cards.exceptions.CardExistException;
import com.cards.cards.model.CardStatus;
import com.cards.cards.model.CardType;
import com.cards.cards.model.Cards;
import com.cards.cards.repository.CardsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
class CardsServiceTest {

    @Mock
    CardsRepository cardsRepository;

    private ICardService iCardService;
    @Mock
    private CardsServiceConfig cardsServiceConfig;

    private  Cards cards;

    @BeforeEach
    void setUp() {
        iCardService = new CardsService(cardsRepository,cardsServiceConfig);
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
    void happyPathForIssueCard() {
        CardDetails expectedCardDetails = new CardDetails();
        expectedCardDetails.setAccountNumber(1598472658);
        expectedCardDetails.setBranchAddress("Pasing");
        expectedCardDetails.setCardType("DEBIT");
        expectedCardDetails.setDailyLimit(3);
        expectedCardDetails.setExpiredDate(LocalDateTime.now().plusYears(5));
        expectedCardDetails.setPersonalAddress("Planegger");
        when(cardsRepository.findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT)).thenReturn(null);
        when(cardsRepository.saveAndFlush(any(Cards.class))).thenReturn(cards);
        CardDetails actualCardDetails = iCardService.issueCard(expectedCardDetails);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getAccountNumber()).isEqualTo(expectedCardDetails.getAccountNumber());
        verify(cardsRepository,times(1)).findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT);
        verify(cardsRepository,times(1)).saveAndFlush(any(Cards.class));
    }
    @Test
    void failedPathForIssueCard() {
        CardDetails expectedCardDetails = new CardDetails();
        expectedCardDetails.setAccountNumber(1598472658);
        expectedCardDetails.setBranchAddress("Pasing");
        expectedCardDetails.setCardType("DEBIT");
        expectedCardDetails.setDailyLimit(3);
        expectedCardDetails.setExpiredDate(LocalDateTime.now().plusYears(5));
        expectedCardDetails.setPersonalAddress("Planegger");
        when(cardsRepository.findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT)).thenReturn(cards);
        assertThatThrownBy(() -> {
            iCardService.issueCard(expectedCardDetails);
        }).isInstanceOf(CardExistException.class).hasMessage("Card already exist");
        verify(cardsRepository,times(1)).findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT);
    }
    @Test
    void happyPathForActivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(cards);
        when(cardsRepository.saveAndFlush(any(Cards.class))).thenReturn(cards);
        CardDetails actualCardDetails = iCardService.activate(cardRequest);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getStatus()).isEqualTo("ACTIVE");
        verify(cardsRepository,times(1)).findByCardNumber(cardRequest.getCardNumber());
        verify(cardsRepository,times(1)).saveAndFlush(any(Cards.class));
    }
    @Test
    void failedPathForActivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(null);
        assertThatThrownBy(() -> {
            iCardService.activate(cardRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository,times(1)).findByCardNumber(cardRequest.getCardNumber());
    }

    @Test
    void happyPathForDeactivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        cards.setStatus(CardStatus.INACTIVE);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(cards);
        when(cardsRepository.saveAndFlush(any(Cards.class))).thenReturn(cards);
        CardDetails actualCardDetails = iCardService.deactivate(cardRequest);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getStatus()).isEqualTo("INACTIVE");
        verify(cardsRepository,times(1)).findByCardNumber(cardRequest.getCardNumber());
        verify(cardsRepository,times(1)).saveAndFlush(any(Cards.class));
    }
    @Test
    void failedPathForDeactivate() {
        CardRequest cardRequest = new CardRequest();
        cardRequest.setCardNumber(584792158);
        when(cardsRepository.findByCardNumber(cardRequest.getCardNumber())).thenReturn(null);
        assertThatThrownBy(() -> {
            iCardService.deactivate(cardRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository,times(1)).findByCardNumber(cardRequest.getCardNumber());
    }

    @Test
    void happyPathToFetchAllCardDetails() {
        long accountNumber = 1598472658;
        List<Cards> cardsList = new ArrayList<>();
        cardsList.add(cards);
        when(cardsRepository.findByAccountNumber(accountNumber)).thenReturn(cardsList);
        List<CardDetails> actualCardsList = iCardService.fetchAllCardDetails(accountNumber);
        assertThat(actualCardsList).isNotNull();
        assertThat(actualCardsList.size()).isEqualTo(1);
        verify(cardsRepository,times(1)).findByAccountNumber(accountNumber);
    }
    @Test
    void failedPathToFetchAllCardDetails() {
        long accountNumber = 1598472658;
        List<Cards> cardsList = new ArrayList<>();
        when(cardsRepository.findByAccountNumber(accountNumber)).thenReturn(cardsList);
        assertThatThrownBy(() -> {
            iCardService.fetchAllCardDetails(accountNumber);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository,times(1)).findByAccountNumber(accountNumber);
    }
    @Test
    void happyPathToFetchCardDetailsByCardNumber() {
        long cardNumber = 58426791;
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(cards);
        CardDetails actualCardsDetails = iCardService.fetchCardDetailsByCardNumber(cardNumber);
        assertThat(actualCardsDetails).isNotNull();
        assertThat(actualCardsDetails.getCardType()).isEqualTo(cards.getCardType().name());
        verify(cardsRepository,times(1)).findByCardNumber(cardNumber);
    }
    @Test
    void failedPathToFetchCardDetailsByCardNumber() {
        long cardNumber = 58426791;
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iCardService.fetchCardDetailsByCardNumber(cardNumber);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository,times(1)).findByCardNumber(cardNumber);
    }
    @Test
    void happyPathToExtendExpiredDate() {
        long cardNumber = 58426791;
        CardRequest cardExpiredDateRequest = new CardRequest();
        cardExpiredDateRequest.setCardNumber(cardNumber);
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(cards);
        CardDetails actualCardsDetails = iCardService.extendExpiredDate(cardExpiredDateRequest);
        assertThat(actualCardsDetails).isNotNull();
        assertThat(actualCardsDetails.getExpiredDate()).isEqualToIgnoringMinutes(LocalDateTime.now().plusYears(4));
        verify(cardsRepository,times(1)).findByCardNumber(cardNumber);
    }
    @Test
    void failedPathToExtendExpiredDate() {
        long cardNumber = 58426791;
        CardRequest cardExpiredDateRequest = new CardRequest();
        cardExpiredDateRequest.setCardNumber(cardNumber);
        when(cardsRepository.findByCardNumber(cardNumber)).thenReturn(null);
        assertThatThrownBy(() -> {
            iCardService.extendExpiredDate(cardExpiredDateRequest);
        }).isInstanceOf(CardDetailsNotFoundException.class).hasMessage("Card details not found with given card number");
        verify(cardsRepository,times(1)).findByCardNumber(cardNumber);
    }
}
