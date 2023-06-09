package com.laurynas.uzduotis.api.models;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "messages")
public class MessageModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long timestamp;

    public MessageModel(Long userId, String message) {
        this.message = message;
        this.userId = userId;
        this.timestamp = Instant.now().getEpochSecond();
    }

    public MessageModel() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
