package com.airevents.error;

public class AirEventsException extends RuntimeException {
    private ErrorCode errorCode;

    public AirEventsException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
