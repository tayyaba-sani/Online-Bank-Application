package com.accounts.accounts.controller;

import com.accounts.accounts.dtos.AccountCardLoanDetails;
import com.accounts.accounts.dtos.AccountDetails;
import com.accounts.accounts.dtos.Transaction;
import com.accounts.accounts.service.AccountsService;
import com.accounts.accounts.service.IAccountService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/account")
@Validated
public class AccountsController {

    private static final Logger logger = LoggerFactory.getLogger(AccountsController.class);

    private final IAccountService accountsService;

    public AccountsController(IAccountService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping("/create-account")
    @ApiOperation(value = "Create account",notes = "Provide account details to create account.", response = AccountDetails.class )
    public ResponseEntity<AccountDetails> createAccount(@Valid @RequestBody AccountDetails accountDetails) {
        return new ResponseEntity(accountsService.createAccount(accountDetails), HttpStatus.CREATED);
    }
    @DeleteMapping("/delete-account/{account-number}")
    @ApiOperation(value = "Delete account",notes = "Provide account number to delete specific account.", response = String.class )
    public ResponseEntity<String> deleteAccount(@PathVariable("account-number")
                                                    @Range(min = 1000000000L, max = 9999999999L)
                                                            Long accountNumber) {
        return new ResponseEntity(accountsService.deleteAccount(accountNumber), HttpStatus.OK);
    }
    @GetMapping("/account-details/{account-number}")
    @ApiOperation(value = "Find account details by account number",
            notes = "Provide account number to look up specific account details"
            , response = AccountDetails.class )
    public ResponseEntity<AccountDetails> getAccountDetails(@PathVariable("account-number")
                                                                @Range(min = 1000000000L, max = 9999999999L)
                                                                        Long accountNumber) {
        return new ResponseEntity(accountsService.getAccountDetails(accountNumber), HttpStatus.OK);
    }
    @GetMapping("/account-details-with-loans-cards/{account-number}")
    @ApiOperation(value = "Find account details with loans and cards details by account number",
            notes = "Provide account number to look up specific account details along with loans and cards details"
            , response = AccountCardLoanDetails.class )
    public ResponseEntity<AccountCardLoanDetails> getAccountDetailsWithLoanCards(@PathVariable("account-number")
                                                                                     @Range(min = 1000000000L, max = 9999999999L)
                                                                                             Long accountNumber) {
        return new ResponseEntity(accountsService.getAccountDetailsWithLoanCards(accountNumber), HttpStatus.OK);
    }
    @PostMapping("/debit")
    @ApiOperation(value = "Debit transaction",
            notes = "Provide account number and amount to do debit transaction."
            , response = AccountCardLoanDetails.class )
    public ResponseEntity<AccountDetails> debit(@Valid @RequestBody Transaction transaction) {
        return new ResponseEntity(accountsService.debit(transaction.getAccountNumber(), transaction.getAmount()), HttpStatus.OK);
    }
    @PostMapping("/credit")
    @ApiOperation(value = "Credit transaction",
            notes = "Provide account number and amount to do credit transaction."
            , response = AccountDetails.class )
    public ResponseEntity<AccountDetails> credit(@Valid @RequestBody Transaction transaction) {
        return new ResponseEntity(accountsService.credit(transaction.getAccountNumber(), transaction.getAmount()), HttpStatus.OK);
    }
    @GetMapping("/show-balance/{account-number}")
    @ApiOperation(value = "Find balance by account number",
            notes = "Provide account number to show current balance"
            , response = AccountDetails.class )
    public ResponseEntity<AccountDetails> showBalance(@PathVariable("account-number")
                                                          @Range(min = 1000000000L, max = 9999999999L)
                                                                  Long accountNumber) {
        return new ResponseEntity(accountsService.showBalance(accountNumber), HttpStatus.OK);
    }
    @GetMapping("/show-transactions/{account-number}")
    @ApiOperation(value = "Find all transactions by account number",
            notes = "Provide account number to show all transaction"
            , response = Transaction.class )
    public ResponseEntity<List<Transaction>> showTransactions(@PathVariable("account-number")
                                                                  @Range(min = 1000000000L, max = 9999999999L)
                                                                          Long accountNumber) {
        return new ResponseEntity(accountsService.showTransactions(accountNumber), HttpStatus.OK);
    }


}
