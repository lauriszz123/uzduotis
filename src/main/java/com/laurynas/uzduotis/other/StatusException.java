package com.laurynas.uzduotis.other;

import org.springframework.http.HttpStatus;

public class StatusException extends Exception {
    HttpStatus status;
    public StatusException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }
    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
