package com.cards.cards.controller;

import com.cards.cards.dtos.CardRequest;
import com.cards.cards.dtos.CardDetails;
import com.cards.cards.service.ICardService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("cards")
@Validated
public class CardsController {

    private final ICardService iCardService;

    public CardsController(ICardService iCardService) {
        this.iCardService = iCardService;
    }

    @PostMapping("/issue-card")
    @ApiOperation(value = "Issue card",notes = "Provide card details to issue card.", response = CardDetails.class )
    public ResponseEntity<CardDetails> issueCard(@Valid @RequestBody CardDetails cardDetails) {
        return new ResponseEntity(iCardService.issueCard(cardDetails), HttpStatus.OK);
    }

    @PutMapping("/activate")
    @ApiOperation(value = "Activate card",notes = "Provide card number to activate card.", response = CardDetails.class )
    public ResponseEntity<CardDetails> activate(@Valid @RequestBody CardRequest cardRequest) {
        return new ResponseEntity(iCardService.activate(cardRequest), HttpStatus.OK);
    }

    @PutMapping("/deactivate")
    @ApiOperation(value = "Deactivate card",notes = "Provide card number to deactivate card.", response = CardDetails.class )
    public ResponseEntity<CardDetails> deactivate(@Valid @RequestBody CardRequest cardRequest) {
        return new ResponseEntity(iCardService.deactivate(cardRequest), HttpStatus.OK);
    }

    @GetMapping("/card-details/{account-number}")
    @ApiOperation(value = "Find all card details by account number",
            notes = "Provide account number to find all card details.", response = CardDetails.class )
    public ResponseEntity<List<CardDetails>> fetchAllCardDetails(@PathVariable("account-number")
                                                                     @Range(min = 1000000000L, max = 9999999999L)
                                                                             long accountNumber) {
        return new ResponseEntity(iCardService.fetchAllCardDetails(accountNumber), HttpStatus.OK);
    }

    @GetMapping("/card-details-by-cardNumber/{card-number}")
    @ApiOperation(value = "Find card details by card number",
            notes = "Provide card number to find specific card details.", response = CardDetails.class )
    public ResponseEntity<CardDetails> fetchCardDetailsByCardNumber(@PathVariable("card-number")
                                                                        @Range(min = 1000000000L, max = 9999999999L)
                                                                                long cardNumber) {
        return new ResponseEntity(iCardService.fetchCardDetailsByCardNumber(cardNumber), HttpStatus.OK);
    }

    @PutMapping("/extend-expiredDate")
    @ApiOperation(value = "Extend expired date",
            notes = "Provide card number to extend expired date.", response = CardDetails.class )
    public ResponseEntity<CardDetails> extendExpiredDate(@Valid @RequestBody CardRequest cardRequest)
    {
        return new ResponseEntity(iCardService.extendExpiredDate(cardRequest), HttpStatus.OK);
    }

}
