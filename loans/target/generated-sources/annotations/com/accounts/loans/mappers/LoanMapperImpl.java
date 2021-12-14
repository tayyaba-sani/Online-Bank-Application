package com.accounts.loans.mappers;

import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.model.Loans;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2021-12-11T21:35:27+0100",
    comments = "version: 1.4.2.Final, compiler: javac, environment: Java 11.0.8 (JetBrains s.r.o.)"
)
public class LoanMapperImpl implements LoanMapper {

    @Override
    public LoanDetails entityToDto(Loans loans) {
        if ( loans == null ) {
            return null;
        }

        LoanDetails loanDetails = new LoanDetails();

        loanDetails.setLoanNumber( loans.getLoanNumber() );
        loanDetails.setTotalLoan( loans.getTotalLoan() );
        loanDetails.setAmountPaid( loans.getAmountPaid() );
        loanDetails.setAccountNumber( loans.getAccountNumber() );
        loanDetails.setAmount( loans.getAmount() );
        loanDetails.setBranchAddress( loans.getBranchAddress() );
        if ( loans.getLoanType() != null ) {
            loanDetails.setLoanType( loans.getLoanType().name() );
        }

        return loanDetails;
    }

    @Override
    public List<LoanDetails> entityListToDtoList(List<Loans> loans) {
        if ( loans == null ) {
            return null;
        }

        List<LoanDetails> list = new ArrayList<LoanDetails>( loans.size() );
        for ( Loans loans1 : loans ) {
            list.add( entityToDto( loans1 ) );
        }

        return list;
    }
}
