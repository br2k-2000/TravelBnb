package com.travelbnb.Exception;

import com.travelbnb.payload.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@ControllerAdvice
public class ExceptionHandling {

        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ErrorDetails> resourceNotFoundException(
                ResourceNotFoundException ex,
                WebRequest webRequest
        ){
            ErrorDetails error = new ErrorDetails(
                    ex.getMessage(),
                    new Date(),
                    webRequest.getDescription(true)
            );
            return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
        }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> globalExceptionException(
            Exception ex,
            WebRequest webRequest
    ){
        ErrorDetails error = new ErrorDetails(
                ex.getMessage(),
                new Date(),
                webRequest.getDescription(false)
        );
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
    @RestControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(CountryAlreadyExistsException.class)
        public ResponseEntity<String> handleCountryAlreadyExistsException(CountryAlreadyExistsException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }


        @ExceptionHandler(ConcurrencyException.class)
        public ResponseEntity<String> handleConcurrencyException(ConcurrencyException ex) {
//            ErrorDetails error = new ErrorDetails(
//                    ex.getMessage(),
//                    new Date(),
//                    "Concurrency Issue"

            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
            }
        }
        // Handle other exceptions if needed
    }

