package com.accounts.loans.service;

import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.exceptions.ExceptionMessages;
import com.accounts.loans.exceptions.LoanDetailsNotFoundException;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DeleteLoanService implements IDeleteLoanService {

    private static final Logger logger = LoggerFactory.getLogger(DeleteLoanService.class);

    private final LoansRepository loansRepository;

    public DeleteLoanService(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    @Override
    public Boolean deleteLoan(DeleteLoanRequest request) {
        logger.info("DeleteLoanService: deleteLoan: Request: " + request.toString());
        Loans loans = loansRepository.findByAccountNumberAndLoanType(request.getAccountNumber(),
                checkLoanType(request.getLoanType().toUpperCase()));
        if (!Objects.isNull(loans)) {
            loansRepository.delete(loans);
            logger.info("DeleteLoanService: deleteLoan: Response: " + "Loan details successfully deleted");
            return true;
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
