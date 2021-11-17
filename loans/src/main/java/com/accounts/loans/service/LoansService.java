package com.accounts.loans.service;

import com.accounts.loans.config.LoansServiceConfig;
import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.exceptions.ExceptionMessages;
import com.accounts.loans.exceptions.LoanDetailsNotFoundException;
import com.accounts.loans.mappers.LoanMapper;
import com.accounts.loans.model.LoanStatus;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LoansService implements ILoanService {

    private static final Logger logger = LoggerFactory.getLogger(LoansService.class);

    private final LoansRepository loansRepository;

    private LoansServiceConfig loansServiceConfig;

    public LoansService(LoansRepository loansRepository,LoansServiceConfig loansServiceConfig) {
        this.loansRepository = loansRepository;
        this.loansServiceConfig = loansServiceConfig;
    }
    @Override
    public LoanDetails applyForLoan(LoanDetails loanDetails) {
        logger.info("LoansService: applyForLoan: Request: "+loanDetails.toString());
        Loans previousLoans = loansRepository.findByAccountNumberAndLoanType(loanDetails.getAccountNumber(), checkLoanType(loanDetails.getLoanType().toUpperCase()));
        Loans newLoan;
        if (Objects.isNull(previousLoans)) {
            newLoan = loansRepository.saveAndFlush(setLoans(loanDetails));
        } else {
            previousLoans.setAmount(previousLoans.getAmount() + loanDetails.getAmount());
            previousLoans.setTotalLoan(loanDetails.getAmount() + calculateInterest(loanDetails.getAmount()) + previousLoans.getTotalLoan());
            newLoan = loansRepository.saveAndFlush(previousLoans);
        }
        LoanDetails newLoanDetails = LoanMapper.INSTANCE.entityToDto(newLoan);
        logger.info("LoansService: applyForLoan: Response: "+newLoanDetails.toString());
        return newLoanDetails;

    }
      @Override
    public LoanDetails paidLoanAmount(LoanDetails loanDetails) {
          logger.info("LoansService: paidLoanAmount: Request: "+loanDetails.toString());
        Loans loans = loansRepository.findByAccountNumberAndLoanType(loanDetails.getAccountNumber(),
                checkLoanType(loanDetails.getLoanType().toUpperCase()));
        if (!Objects.isNull(loans)) {
            int paidAmount = loans.getAmountPaid() + loanDetails.getAmount();
            int totalAmount = loans.getTotalLoan() - loanDetails.getAmount();
            loans.setAmountPaid(paidAmount);
            loans.setTotalLoan(totalAmount);
            Loans updateLoan = loansRepository.saveAndFlush(loans);
            LoanDetails newLoanDetails = LoanMapper.INSTANCE.entityToDto(updateLoan);
            logger.info("LoansService: paidLoanAmount: Response: "+newLoanDetails.toString());
            return newLoanDetails;
        }
        throw new LoanDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_ACCOUNT.toString());
    }

    @Override
    public String deleteLoan(DeleteLoanRequest request) {
        logger.info("LoansService: deleteLoan: Request: "+request.toString());
        Loans loans = loansRepository.findByAccountNumberAndLoanType(request.getAccountNumber(),
                checkLoanType(request.getLoanType().toUpperCase()));
        if (!Objects.isNull(loans)) {
            loansRepository.delete(loans);
            logger.info("LoansService: deleteLoan: Response: "+"Loan details successfully deleted");
            return "Loan details successfully deleted";
        }
        throw new LoanDetailsNotFoundException(ExceptionMessages.NOT_FOUND_BY_ACCOUNT.toString());
    }
    @Override
    public List<LoanDetails> getLoanDetails(long accountNumber) {
        logger.info("LoansService: getLoanDetails: Request: "+accountNumber);
        List<Loans> loansList = loansRepository.findByAccountNumber(accountNumber);
        if (loansList.isEmpty()) {
            return new ArrayList<>();
        }
        List<LoanDetails> loanDetailsList = LoanMapper.INSTANCE.entityListToDtoList(loansList);
        logger.info("LoansService: getLoanDetails: Response: "+loanDetailsList.toString());
        return loanDetailsList;
    }
    private Loans setLoans(LoanDetails loanDetails){
        Loans loans = new Loans();
        loans.setAccountNumber(loanDetails.getAccountNumber());
        loans.setAmount(loanDetails.getAmount());
        loans.setAmountPaid(0);
        loans.setTotalLoan(loanDetails.getAmount() + calculateInterest(loanDetails.getAmount()));
        loans.setBranchAddress(loanDetails.getBranchAddress());
        loans.setLoanType(checkLoanType(loanDetails.getLoanType().toUpperCase()));
        loans.setStartDate(LocalDateTime.now());
        loans.setStatus(LoanStatus.ACTIVE);
        return loans;
    }
    private int calculateInterest(int amount) {
        logger.info("LoansService: calculateInterest: loansServiceConfig getInterestRate: "+loansServiceConfig.getInterestRate());
        return (amount * loansServiceConfig.getInterestRate()) / 100;
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
