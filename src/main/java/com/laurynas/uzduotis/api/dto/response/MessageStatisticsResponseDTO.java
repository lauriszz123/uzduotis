package com.laurynas.uzduotis.api.dto.response;

import com.laurynas.uzduotis.other.UnixTimestampConverter;

public class MessageStatisticsResponseDTO {
    private String username;
    private int messageCount;
    private String firstMessageTimestamp;
    private String lastMessageTimestamp;
    private int averageMessageLength;
    private String lastMessageText;

    private String errorMessage;

    public MessageStatisticsResponseDTO() { }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String getFirstMessageTimestamp() {
        return firstMessageTimestamp;
    }

    public void setFirstMessageTimestamp(Long firstMessageTimestamp) {
        this.firstMessageTimestamp = UnixTimestampConverter.convertUnixTimestampToString(firstMessageTimestamp);
    }

    public String getLastMessageTimestamp() {
        return lastMessageTimestamp;
    }

    public void setLastMessageTimestamp(Long lastMessageTimestamp) {
        this.lastMessageTimestamp = UnixTimestampConverter.convertUnixTimestampToString(lastMessageTimestamp);
    }

    public int getAverageMessageLength() {
        return averageMessageLength;
    }

    public void setAverageMessageLength(int averageMessageLength) {
        this.averageMessageLength = averageMessageLength;
    }

    public String getLastMessageText() {
        return lastMessageText;
    }

    public void setLastMessageText(String lastMessageText) {
        this.lastMessageText = lastMessageText;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
