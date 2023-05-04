package com.example.auctionsystem.controller.handler;

import com.example.auctionsystem.exception.LotNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NotFoundExceptionHandler {
    @ExceptionHandler(LotNotFoundException.class)
    public ResponseEntity<?> lotNotFoundHand(){
return ResponseEntity.status(400).build();
    }
}
