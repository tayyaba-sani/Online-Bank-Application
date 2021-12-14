package com.accounts.accounts.mappers;

import com.accounts.accounts.dtos.TransactionRequest;
import com.accounts.accounts.dtos.TransactionResponse;
import com.accounts.accounts.model.Accounts;
import com.accounts.accounts.model.Operation;
import com.accounts.accounts.model.OperationType;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.UUID;

@Mapper
public interface OperationMapper {

    OperationMapper INSTANCE = Mappers.getMapper(OperationMapper.class);
    @Mappings({
            @Mapping(source = "operation.operationId", target = "transactionId"),
            @Mapping(source = "operation.typeOperation", target = "operationType"),
            @Mapping(source = "operation.createDatetime", target = "createdDate"),
            @Mapping(source = "operation.account.accountNumber", target = "accountNumber"),
    })
    @Named("entityToDtoResponse")
    TransactionResponse entityToDtoResponse(Operation operation);

    @IterableMapping(qualifiedByName = "entityToDtoResponse")
    @Named("entityListToDtoList")
    List<TransactionResponse> entityListToDtoList(List<Operation> operationList);

    default Operation DTOToEntity(Accounts accounts, double amount, double actualAmount,OperationType operationType)
    {
        Operation operation = new Operation();
        operation.setTypeOperation(operationType);
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setAmount(amount);
        accounts.setActualBalance(actualAmount);
        operation.setAccount(accounts);
        return operation;
    }
}
