package com.accounts.accounts.client;

import com.accounts.accounts.dtos.CardDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient("cards")
public interface CardsClient {

    @RequestMapping(method = RequestMethod.GET, value = "/cards/card-details/{account-number}", consumes = "application/json")
    List<CardDetails> fetchAllCardDetails(@PathVariable("account-number") long accountNumber);
}
