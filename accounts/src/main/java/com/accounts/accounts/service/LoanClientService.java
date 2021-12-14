package com.accounts.accounts.service;

import com.accounts.accounts.client.LoansClient;
import com.accounts.accounts.dtos.CardDetails;
import com.accounts.accounts.dtos.LoanDetails;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LoanClientService {
    private static final Logger logger = LoggerFactory.getLogger(LoanClientService.class);
    private final LoansClient loansClient;

    public LoanClientService(LoansClient loansClient) {
        this.loansClient = loansClient;
    }

    @Retry(name = "retryForLoanDetails", fallbackMethod = "getLoanDetailsFallBack")
    public List<LoanDetails> getLoanDetails(long accountNumber)
    {
        return loansClient.getLoansDetails(accountNumber);
    }
    private List<CardDetails> getLoanDetailsFallBack(long accountNumber, Throwable t) {
        logger.info("LoanClientService: getLoanDetailsFallBack: Request: " + accountNumber);
        logger.info("LoanClientService: getLoanDetailsFallBack: Response: ");
        return null;
    }
}
