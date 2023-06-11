package com.laurynas.uzduotis.other;

import org.springframework.http.HttpStatus;

public class StatusException extends Exception {
    private final HttpStatus status;
    public StatusException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }
}
