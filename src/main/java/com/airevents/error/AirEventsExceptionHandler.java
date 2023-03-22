package com.airevents.error;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class AirEventsExceptionHandler {

    @ExceptionHandler(AirEventsException.class)
    public ResponseEntity<ErrorResponse> handleDeviceActionException(AirEventsException ex) {
        return ResponseEntity.badRequest().body(new ErrorResponse(ex.getErrorCode(), ex.getMessage()));
    }
}
