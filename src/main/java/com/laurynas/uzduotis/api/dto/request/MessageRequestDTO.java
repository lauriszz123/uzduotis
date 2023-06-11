package com.laurynas.uzduotis.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MessageRequestDTO {
    private String message;

    public MessageRequestDTO(@JsonProperty String message) {
        this.message = message;
    }

    public MessageRequestDTO() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
