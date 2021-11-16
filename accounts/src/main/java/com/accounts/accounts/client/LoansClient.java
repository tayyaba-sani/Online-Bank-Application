package com.accounts.accounts.client;

import com.accounts.accounts.dtos.LoanDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("loans")
public interface LoansClient {

    @RequestMapping(method = RequestMethod.GET, value = "/loans/loan-details/{account-number}", consumes = "application/json")
    List<LoanDetails> getLoansDetails(@PathVariable("account-number") long accountNumber);
}
