package com.cards.cards.repository;

import com.cards.cards.model.CardStatus;
import com.cards.cards.model.CardType;
import com.cards.cards.model.Cards;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class CardsRepositoryTest {

    @Autowired
    CardsRepository cardsRepository;

    private Cards cards;

    @BeforeEach
    void setUp() {
        cards = new Cards();
        cards.setStatus(CardStatus.ACTIVE);
        cards.setExpiredDate(LocalDateTime.now().plusYears(5));
        cards.setAccountNumber(1598472658);
        cards.setBranchAddress("Pasing");
        cards.setCardNumber(457898652);
        cards.setDailyLimit(50);
        cards.setCardType(CardType.DEBIT);
        cards.setPersonalAddress("Planegger");
        cardsRepository.saveAndFlush(cards);
    }

    @Test
    void happyPathToFindByAccountNumber() {
        List<Cards> actualCards = cardsRepository.findByAccountNumber(1598472658);
        assertThat(actualCards).isNotNull();
        assertThat(actualCards.get(0).getAccountNumber()).isEqualTo(1598472658);
    }
    @Test
    void failedPathToFindByAccountNumber() {
        List<Cards> actualCards = cardsRepository.findByAccountNumber(1598472657);
        assertThat(actualCards.size()).isEqualTo(0);
    }

    @Test
    void happyPathToFindByCardNumber() {
        Cards actualCard = cardsRepository.findByCardNumber(cards.getCardNumber());
        assertThat(actualCard).isNotNull();
        assertThat(actualCard.getAccountNumber()).isEqualTo(1598472658);
    }
    @Test
    void failedPathToFindByCardNumber() {
        Cards actualCard = cardsRepository.findByCardNumber(58476);
        assertThat(actualCard).isNull();
    }

    @Test
    void happyPathToFindByAccountNumberAndCardType() {
        Cards actualCard = cardsRepository.findByAccountNumberAndCardType(cards.getAccountNumber(),cards.getCardType());
        assertThat(actualCard).isNotNull();
        assertThat(actualCard.getAccountNumber()).isEqualTo(1598472658);
    }

    @Test
    void failedPathToFindByAccountNumberAndCardType() {
        Cards actualCard = cardsRepository.findByAccountNumberAndCardType(7854,cards.getCardType());
        assertThat(actualCard).isNull();
    }
}