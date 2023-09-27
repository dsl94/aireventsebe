package com.airevents.error;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RcnExceptionHandler {

    @ExceptionHandler(RcnException.class)
    public ResponseEntity<ErrorResponse> handleDeviceActionException(RcnException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }
}
