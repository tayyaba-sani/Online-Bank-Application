package com.accounts.accounts.exceptions;


import com.accounts.accounts.dtos.ExceptionObj;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ExceptionResponseHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionObj> HandleAllException(Exception ex,
                                                           WebRequest webRequest) {
            logger.error("ExceptionResponseHandler: HandleAllException Occurred: "+ex.getMessage());
            ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), webRequest.getDescription(false));
            return new ResponseEntity<>(exceptionObjResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ExceptionObj> handleAccountNotFoundExceptions(AccountNotFoundException ex, WebRequest request) {
        logger.error("ExceptionResponseHandler: AccountNotFoundException Occurred: "+ex.getMessage());
        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ExceptionObj> handleInsufficientBalanceException(InsufficientBalanceException ex, WebRequest request) {
        logger.error("ExceptionResponseHandler: InsufficientBalanceException Occurred: "+ex.getMessage());
        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        logger.error("ExceptionResponseHandler: handleMethodArgumentNotValid Occurred: "+ex.getMessage());
        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), ex.getBindingResult().toString());
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.BAD_REQUEST);
    }
}
