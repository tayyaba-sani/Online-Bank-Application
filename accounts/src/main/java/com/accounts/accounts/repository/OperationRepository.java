package com.accounts.accounts.repository;

import com.accounts.accounts.model.Accounts;
import com.accounts.accounts.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {

}
