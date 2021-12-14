package com.accounts.loans.service;

import com.accounts.loans.config.LoansServiceConfig;
import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.mappers.LoanMapper;
import com.accounts.loans.model.LoanStatus;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ApplyLoanService implements IApplyLoanService {
    private static final Logger logger = LoggerFactory.getLogger(ApplyLoanService.class);

    private final LoansRepository loansRepository;
    private LoansServiceConfig loansServiceConfig;

    public ApplyLoanService(LoansRepository loansRepository, LoansServiceConfig loansServiceConfig) {
        this.loansRepository = loansRepository;
        this.loansServiceConfig = loansServiceConfig;
    }

    @Override
    public LoanDetails applyForLoan(LoanDetails loanDetails) {
        logger.info("ApplyLoanService: applyForLoan: Request: " + loanDetails.toString());
        Loans previousLoans = loansRepository.findByAccountNumberAndLoanType(loanDetails.getAccountNumber(), checkLoanType(loanDetails.getLoanType().toUpperCase()));
        Loans newLoan;
        if (Objects.isNull(previousLoans)) {
            Loans loans = LoanMapper.INSTANCE.DtoToEntity(loanDetails,calculateInterest(loanDetails.getAmount()),checkLoanType(loanDetails.getLoanType().toUpperCase()));
            newLoan = loansRepository.saveAndFlush(loans);
        } else {
            previousLoans.setAmount(previousLoans.getAmount() + loanDetails.getAmount());
            previousLoans.setTotalLoan(loanDetails.getAmount() + calculateInterest(loanDetails.getAmount()) + previousLoans.getTotalLoan());
            newLoan = loansRepository.saveAndFlush(previousLoans);
        }
        LoanDetails newLoanDetails = LoanMapper.INSTANCE.entityToDto(newLoan);
        logger.info("ApplyLoanService: applyForLoan: Response: " + newLoanDetails.toString());
        return newLoanDetails;
    }

    private LoanType checkLoanType(String loanType) {
        if (loanType.equals(LoanType.CAR.name())) {
            return LoanType.CAR;
        } else if (loanType.equals(LoanType.HOME.name())) {
            return LoanType.HOME;
        } else {
            return LoanType.PERSONAL;
        }
    }

    private int calculateInterest(int amount) {
        logger.info("LoansService: calculateInterest: loansServiceConfig getInterestRate: " + loansServiceConfig.getInterestRate());
        return (amount * loansServiceConfig.getInterestRate()) / 100;
    }

}
