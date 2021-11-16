package com.accounts.accounts.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message){
        super(message);
    }
}
