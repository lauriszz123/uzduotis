package com.laurynas.uzduotis.api.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginRequestDTO {
    private String username;
    private String password;

    public LoginRequestDTO(@JsonProperty String username, @JsonProperty String password) {
        this.username = username;
        this.password = password;
    }


    public LoginRequestDTO() {}

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
}
