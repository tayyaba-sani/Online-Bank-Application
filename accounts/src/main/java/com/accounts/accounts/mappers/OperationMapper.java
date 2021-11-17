package com.accounts.accounts.mappers;

import com.accounts.accounts.dtos.Transaction;
import com.accounts.accounts.model.Operation;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface OperationMapper {

    OperationMapper INSTANCE = Mappers.getMapper(OperationMapper.class);

    @Mappings({
            @Mapping(source = "operation.operationId", target = "transactionId"),
            @Mapping(source = "operation.typeOperation", target = "operationType"),
            @Mapping(source = "operation.createDatetime", target = "createdDate"),
            @Mapping(source = "operation.account.accountNumber", target = "accountNumber"),
    })
    @Named("entityToDto")
    Transaction entityToDto(Operation operation);

    @IterableMapping(qualifiedByName = "entityToDto")
    @Named("entityListToDtoList")
    List<Transaction> entityListToDtoList(List<Operation> operationList);
}
