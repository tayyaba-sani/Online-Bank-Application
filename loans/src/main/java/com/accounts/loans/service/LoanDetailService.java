package com.accounts.loans.service;

import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.mappers.LoanMapper;
import com.accounts.loans.model.Loans;
import com.accounts.loans.repository.LoansRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoanDetailService implements ILoanDetailService {

    private static final Logger logger = LoggerFactory.getLogger(LoanDetailService.class);

    private final LoansRepository loansRepository;

    public LoanDetailService(LoansRepository loansRepository) {
        this.loansRepository = loansRepository;
    }

    @Override
    public List<LoanDetails> getLoanDetails(long accountNumber) {
        logger.info("LoanDetailService: getLoanDetails: Request: " + accountNumber);
        List<Loans> loansList = loansRepository.findByAccountNumber(accountNumber);
        if (loansList.isEmpty()) {
            return new ArrayList<>();
        }
        List<LoanDetails> loanDetailsList = LoanMapper.INSTANCE.entityListToDtoList(loansList);
        logger.info("LoanDetailService: getLoanDetails: Response: " + loanDetailsList.toString());
        return loanDetailsList;
    }
}
