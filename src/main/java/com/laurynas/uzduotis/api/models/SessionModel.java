package com.laurynas.uzduotis.api.models;

import jakarta.persistence.*;
import org.springframework.context.annotation.Primary;

@Entity
@Table(name = "sessions")
public class SessionModel {
    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long timestamp;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    public SessionModel(String token, Long userId, Long timestamp) {
        this.token = token;
        this.userId = userId;
        this.timestamp = timestamp;
    }

    public SessionModel() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getCreatedTimestamp() {
        return timestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.timestamp = createdTimestamp;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
