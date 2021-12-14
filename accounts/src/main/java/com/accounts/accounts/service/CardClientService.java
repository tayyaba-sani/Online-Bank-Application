package com.accounts.accounts.service;

import com.accounts.accounts.client.CardsClient;
import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.CardDetails;
import com.accounts.accounts.dtos.LoanDetails;
import com.accounts.accounts.mappers.AccountMapper;
import com.accounts.accounts.model.Accounts;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CardClientService {

    private static final Logger logger = LoggerFactory.getLogger(CardClientService.class);
    private final CardsClient cardsClient;

    public CardClientService(CardsClient cardsClient) {
        this.cardsClient = cardsClient;
    }

    @Retry(name = "retryForCardDetails", fallbackMethod = "getCardDetailsFallBack")
    public List<CardDetails> getCardDetails(long accountNumber)
    {
        return cardsClient.fetchAllCardDetails(accountNumber);
    }
    private List<CardDetails> getCardDetailsFallBack(long accountNumber, Throwable t) {
        logger.info("CardClientService: getCardDetailsFallBack: Request: " + accountNumber);
        logger.info("CardClientService: getCardDetailsFallBack: Response: ");
        return null;
    }
}
