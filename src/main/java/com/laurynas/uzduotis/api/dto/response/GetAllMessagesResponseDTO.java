package com.laurynas.uzduotis.api.dto.response;

import com.laurynas.uzduotis.api.dto.MessageDTO;

import java.util.ArrayList;
import java.util.List;

public class GetAllMessagesResponseDTO {
    private List<MessageDTO> messages;
    private String errorMessage;

    public GetAllMessagesResponseDTO() {
        this.messages = new ArrayList<>();
    }

    public void addMessage(MessageDTO message) {
        this.messages.add(message);
    }

    public void setErrorMessage(String error) {
        this.errorMessage = error;
    }

    public List<MessageDTO> getMessages() {
        return messages;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
