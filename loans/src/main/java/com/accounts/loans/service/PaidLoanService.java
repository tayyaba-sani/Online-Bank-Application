package com.accounts.loans.service;

import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.exceptions.ExceptionMessages;
import com.accounts.loans.exceptions.LoanDetailsNotFoundException;
import com.accounts.loans.mappers.LoanMapper;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class PaidLoanService implements IPaidLoanService {
    private static final Logger logger = LoggerFactory.getLogger(PaidLoanService.class);

    private final LoansRepository loansRepository;

    public PaidLoanService(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    @Override
    public LoanDetails paidLoanAmount(LoanDetails loanDetails) {
        logger.info("PaidLoanService: paidLoanAmount: Request: " + loanDetails.toString());
        Loans loans = loansRepository.findByAccountNumberAndLoanType(loanDetails.getAccountNumber(),
                checkLoanType(loanDetails.getLoanType().toUpperCase()));
        if (!Objects.isNull(loans)) {
            int paidAmount = loans.getAmountPaid() + loanDetails.getAmount();
            int totalAmount = loans.getTotalLoan() - loanDetails.getAmount();
            loans.setAmountPaid(paidAmount);
            loans.setTotalLoan(totalAmount);
            Loans updateLoan = loansRepository.saveAndFlush(loans);
            LoanDetails newLoanDetails = LoanMapper.INSTANCE.entityToDto(updateLoan);
            logger.info("PaidLoanService: paidLoanAmount: Response: " + newLoanDetails.toString());
            return newLoanDetails;
        }
        throw new LoanDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_ACCOUNT.toString());
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
}
