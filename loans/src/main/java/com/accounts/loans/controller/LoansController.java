package com.accounts.loans.controller;

import com.accounts.loans.dtos.DeleteLoanRequest;
import com.accounts.loans.dtos.LoanDetails;
import com.accounts.loans.service.IApplyLoanService;
import com.accounts.loans.service.IDeleteLoanService;
import com.accounts.loans.service.ILoanDetailService;
import com.accounts.loans.service.IPaidLoanService;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("loans")
@Validated
public class LoansController {

    private final IApplyLoanService iApplyLoanService;
    private final ILoanDetailService iLoanDetailService;
    private final IDeleteLoanService iDeleteLoanService;
    private final IPaidLoanService iPaidLoanService;

    public LoansController(IApplyLoanService iApplyLoanService, ILoanDetailService iLoanDetailService, IDeleteLoanService iDeleteLoanService, IPaidLoanService iPaidLoanService) {
        this.iApplyLoanService = iApplyLoanService;
        this.iLoanDetailService = iLoanDetailService;
        this.iDeleteLoanService = iDeleteLoanService;
        this.iPaidLoanService = iPaidLoanService;
    }

    @PostMapping("apply-for-loan")
    @ApiOperation(value = "Apply for loan",notes = "Provide loan details to apply loan.", response = LoanDetails.class )
    public ResponseEntity<LoanDetails> applyForLoan(@Valid @RequestBody LoanDetails loanDetails) {
        return new ResponseEntity(iApplyLoanService.applyForLoan(loanDetails), HttpStatus.OK);
    }
    @PostMapping("paid-loan-amount")
    @ApiOperation(value = "Request to paid loan amount",notes = "Provide loan details to pay loan amount.", response = LoanDetails.class )
    public ResponseEntity<LoanDetails> paidLoanAmount(@Valid @RequestBody LoanDetails loanDetails) {
        return new ResponseEntity(iPaidLoanService.paidLoanAmount(loanDetails), HttpStatus.OK);
    }
    @GetMapping("/loan-details/{account-number}")
    @ApiOperation(value = "Find loan details by account number",
            notes = "Provide account number to find all loan details.", response = LoanDetails.class)
    public ResponseEntity<List<LoanDetails>> getLoansDetails(@PathVariable("account-number")
                                                                 @Range(min = 1000000000L, max = 9999999999L)
                                                                         long accountNumber) {
        return new ResponseEntity(iLoanDetailService.getLoanDetails(accountNumber),HttpStatus.OK);
    }
    @DeleteMapping("delete-loan")
    @ApiOperation(value = "Delete loan details by account number and loan type",
            notes = "Provide account number and loan type to delete loan details.", response = String.class)
    public ResponseEntity<Void> deleteLoan(@Valid @RequestBody DeleteLoanRequest deleteLoanRequest) {
        iDeleteLoanService.deleteLoan(deleteLoanRequest);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
