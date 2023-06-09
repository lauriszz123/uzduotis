package com.laurynas.uzduotis.api.dto;

import com.laurynas.uzduotis.other.UnixTimestampConverter;

public class MessageResponseDTO {
    private String username;
    private String message;
    private String timestamp;

    public MessageResponseDTO(String username, String message, Long timestamp) {
        this.message = message;
        this.username = username;
        this.timestamp = UnixTimestampConverter.convertUnixTimestampToString(timestamp);
    }

    public MessageResponseDTO() {}

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = UnixTimestampConverter.convertUnixTimestampToString(timestamp);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
