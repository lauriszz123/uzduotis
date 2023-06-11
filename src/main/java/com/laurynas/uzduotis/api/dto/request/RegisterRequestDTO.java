package com.laurynas.uzduotis.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestDTO {
    private String username;
    private String password;
    private boolean isAdmin;

    public RegisterRequestDTO(@JsonProperty String username, @JsonProperty String password, @JsonProperty boolean isAdmin) {
        this.username = username;
        this.password = password;
        this.isAdmin = isAdmin;
    }

    public RegisterRequestDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
