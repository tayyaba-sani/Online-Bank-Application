package com.accounts.accounts.mappers;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.model.Accounts;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AccountMapper {
    AccountMapper INSTANCE = Mappers.getMapper(AccountMapper.class);

    @Mappings({
            @Mapping(source = "accounts.actualBalance", target = "balance"),
            @Mapping(source = "accounts.customer.name", target = "name"),
            @Mapping(source = "accounts.customer.address", target = "address"),
            @Mapping(source = "accounts.customer.email", target = "email"),
            @Mapping(source = "accounts.customer.contactNumber", target = "contactNumber")

    })
    AccountDetails entityToDto(Accounts accounts);

}
