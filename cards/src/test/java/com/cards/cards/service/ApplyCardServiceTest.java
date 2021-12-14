package com.cards.cards.service;

import com.cards.cards.config.CardsServiceConfig;
import com.cards.cards.dtos.CardDetails;
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

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ApplyCardServiceTest {

    @Mock
    CardsRepository cardsRepository;

    private IApplyCardService iApplyCardService;
    @Mock
    private CardsServiceConfig cardsServiceConfig;

    private Cards cards;

    @BeforeEach
    void setUp() {
        iApplyCardService = new ApplyCardService(cardsRepository, cardsServiceConfig);
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
        CardDetails actualCardDetails = iApplyCardService.issueCard(expectedCardDetails);
        assertThat(actualCardDetails).isNotNull();
        assertThat(actualCardDetails.getAccountNumber()).isEqualTo(expectedCardDetails.getAccountNumber());
        verify(cardsRepository, times(1)).findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT);
        verify(cardsRepository, times(1)).saveAndFlush(any(Cards.class));
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
            iApplyCardService.issueCard(expectedCardDetails);
        }).isInstanceOf(CardExistException.class).hasMessage("Card already exist");
        verify(cardsRepository, times(1)).findByAccountNumberAndCardType(expectedCardDetails.getAccountNumber(),
                CardType.DEBIT);
    }
}