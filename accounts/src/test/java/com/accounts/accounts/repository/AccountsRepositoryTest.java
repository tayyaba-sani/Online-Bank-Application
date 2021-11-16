package com.accounts.accounts.repository;

import com.accounts.accounts.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AccountsRepositoryTest {

    @Autowired
    AccountsRepository accountsRepository;

    @Test
    @DisplayName("Find account details with account number")
    void HappyPathTestFindByAccountNumber() {
        Accounts accounts = new Accounts();
        accounts.setAccountNumber(1598472658);
        accounts.setAccountType(AccountType.SAVING);
        accounts.setActualBalance(500);
        accounts.setBranchAddress("Pasing str");
        accounts.setStatus(AccountStatus.ACTIVE);
        Customer customer = new Customer();
        customer.setAddress("Planegger");
        customer.setContactNumber(4587156);
        customer.setEmail("tayyaba.sani@yahoo.com");
        customer.setName("Tayyaba Sani");
        accounts.setCustomer(customer);
        Operation operation = new Operation();
        operation.setAmount(500);
        operation.setOperationId(UUID.randomUUID().toString());
        operation.setTypeOperation(OperationType.CREDIT);
        accountsRepository.saveAndFlush(accounts);
        Accounts newAccount = accountsRepository.findByAccountNumber(accounts.getAccountNumber());
        assertThat(newAccount).isNotNull();
    }
    @Test
    @DisplayName("Should not give account details with given accountNumber")
    void FailedPathTestFindByAccountNumber() {
        Accounts newAccount = accountsRepository.findByAccountNumber(15487965);
        assertThat(newAccount).isNull();
    }
}