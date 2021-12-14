package com.cards.cards.exceptions;


import com.cards.cards.dtos.ExceptionObj;
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

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionResponseHandler extends ResponseEntityExceptionHandler {
    Logger logger = LoggerFactory.getLogger(ExceptionResponseHandler.class);

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ExceptionObj> HandleAllException(Exception ex,
                                                           WebRequest webRequest) {
            ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), webRequest.getDescription(false));
            return new ResponseEntity<>(exceptionObjResponse, HttpStatus.INTERNAL_SERVER_ERROR);

    }
    @ExceptionHandler(value = ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex,WebRequest request) {
        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(CardDetailsNotFoundException.class)
    public ResponseEntity<ExceptionObj> handleCardDetailsNotFoundExceptions(CardDetailsNotFoundException ex, WebRequest request) {

        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(CardExistException.class)
    public ResponseEntity<ExceptionObj> handleCardExistExceptions(CardExistException ex, WebRequest request) {

        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), request.getDescription(false));
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.CONFLICT);
    }
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ExceptionObj exceptionObjResponse = new ExceptionObj(LocalDateTime.now(), ex.getMessage(), ex.getBindingResult().toString());
        return new ResponseEntity<>(exceptionObjResponse, HttpStatus.BAD_REQUEST);
    }
}
