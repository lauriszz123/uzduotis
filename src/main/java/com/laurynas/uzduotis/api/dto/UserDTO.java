package com.laurynas.uzduotis.api.dto;

public class UserDTO {
    private String username;
    private boolean isAdmin;

    public UserDTO(String username, boolean isAdmin) {
        this.username = username;
        this.isAdmin = isAdmin;
    }

    public UserDTO() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public String toString() {
        return "{" + username + ", " + isAdmin + "}";
    }
}
