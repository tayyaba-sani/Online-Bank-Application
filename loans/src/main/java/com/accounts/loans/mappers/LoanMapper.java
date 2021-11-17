package com.accounts.loans.mappers;

import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.model.Loans;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface LoanMapper {
    LoanMapper INSTANCE = Mappers.getMapper(LoanMapper.class);

    @Named("entityToDto")
    LoanDetails entityToDto(Loans loans);

    @IterableMapping(qualifiedByName = "entityToDto")
    @Named("entityListToDtoList")
    List<LoanDetails> entityListToDtoList(List<Loans> loans);
}
