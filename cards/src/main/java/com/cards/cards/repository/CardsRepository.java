package com.cards.cards.repository;

import com.cards.cards.model.CardType;
import com.cards.cards.model.Cards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CardsRepository extends JpaRepository<Cards,Long> {

    List<Cards> findByAccountNumber(long accountNumber);
    Cards findByCardNumber(long cardNumber);
    Cards findByAccountNumberAndCardType(long accountNumber, CardType cardType);

}
