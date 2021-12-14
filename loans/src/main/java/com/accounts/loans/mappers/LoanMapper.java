package com.accounts.loans.mappers;

import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.model.LoanStatus;
import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Named("entityToDto")
    LoanDetails entityToDto(Loans loans);

    @IterableMapping(qualifiedByName = "entityToDto")
    @Named("entityListToDtoList")
    List<LoanDetails> entityListToDtoList(List<Loans> loans);

    default Loans DtoToEntity(LoanDetails loanDetails, int calculatedInterestAmount, LoanType loanType)
    {
        Loans loans = new Loans();
        loans.setAccountNumber(loanDetails.getAccountNumber());
        loans.setAmount(loanDetails.getAmount());
        loans.setAmountPaid(0);
        loans.setTotalLoan(loanDetails.getAmount() + calculatedInterestAmount);
        loans.setBranchAddress(loanDetails.getBranchAddress());
        loans.setLoanType(loanType);
        loans.setStartDate(LocalDateTime.now());
        loans.setStatus(LoanStatus.ACTIVE);
        return loans;
    }
}
