package com.accounts.loans.repository;

import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class LoansRepositoryTest {

    @Autowired
    LoansRepository loansRepository;

    @BeforeEach
    void setUp() {
        Loans loans = new Loans();
        loans.setAccountNumber(1598472658);
        loans.setAmountPaid(0);
        loans.setBranchAddress("Pasing");
        loans.setLoanType(LoanType.HOME);
        loans.setAmount(5000);
        loans.setTotalLoan(5500);
        loansRepository.saveAndFlush(loans);
    }
    @Test
    void happyPathToFindByAccountNumber() {
        List<Loans> actualLoans = loansRepository.findByAccountNumber(1598472658);
        assertThat(actualLoans).isNotNull();
        assertThat(actualLoans.get(0).getAccountNumber()).isEqualTo(1598472658);
    }
    @Test
    void failedPathToFindByAccountNumber() {
        List<Loans> actualLoans = loansRepository.findByAccountNumber(1598472657);
        assertThat(actualLoans.size()).isEqualTo(0);
    }

    @Test
    void happyPathToFindByAccountNumberAndLoanType() {
        Loans actualLoans = loansRepository.findByAccountNumberAndLoanType(1598472658,LoanType.HOME);
        assertThat(actualLoans).isNotNull();
        assertThat(actualLoans.getAccountNumber()).isEqualTo(1598472658);
    }
    @Test
    void failedPathToFindByAccountNumberAndLoanType() {
        Loans actualLoans = loansRepository.findByAccountNumberAndLoanType(1598472657,LoanType.HOME);
        assertThat(actualLoans).isNull();
    }

}