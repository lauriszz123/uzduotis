package com.laurynas.uzduotis.api.dto.response;

public class LoginResponseDTO {
    private String token;
    private String errorMessage;

    public LoginResponseDTO() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
