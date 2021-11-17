package com.accounts.loans.exceptions;

public class LoanDetailsNotFoundException extends RuntimeException{
   public LoanDetailsNotFoundException(String message)
   {
       super(message);
   }
}
