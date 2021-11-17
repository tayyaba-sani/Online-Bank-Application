package com.accounts.loans.repository;

import com.accounts.loans.model.LoanType;
import com.accounts.loans.model.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoansRepository extends JpaRepository<Loans,Long> {

    List<Loans> findByAccountNumber(long accountNumber);
    @Query("select l from Loans l where l.accountNumber = ?1 and l.loanType = ?2")
    Loans findByAccountNumberAndLoanType(long accountNumber, LoanType loanType);

}
