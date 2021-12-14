package com.accounts.accounts.repository;

import com.accounts.accounts.model.Accounts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountsRepository extends JpaRepository<Accounts, Long> {

    @Query("select a from Accounts a where a.accountNumber = ?1")
    Accounts findByAccountNumber(long accountNumber);

}
