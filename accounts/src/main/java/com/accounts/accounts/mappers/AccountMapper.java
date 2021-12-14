package com.accounts.accounts.mappers;

import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.model.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

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

    default Accounts DtoToEntity(AccountDetails accountDetails)
    {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(accountDetails.getAccountNumber());
        accounts.setAccountType(AccountType.SAVING.name().equals(accountDetails.getAccountType().toUpperCase()) ? AccountType.SAVING: AccountType.FIXED);
        accounts.setActualBalance(accountDetails.getBalance());
        accounts.setBranchAddress(accountDetails.getBranchAddress());
        accounts.setStatus(AccountStatus.ACTIVE);
        Customer customer = new Customer();
        customer.setAddress(accountDetails.getAddress());
        customer.setContactNumber(accountDetails.getContactNumber());
        customer.setEmail(accountDetails.getEmail());
        customer.setName(accountDetails.getName());
        accounts.setCustomer(customer);
        Operation operation = new Operation();
        operation.setAmount(accountDetails.getBalance());
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setTypeOperation(OperationType.CREDIT);
        accounts.doOperation(operation);
        accounts.setOperations(accounts.getOperations());
        return accounts;
    }
}
