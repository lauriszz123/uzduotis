package com.laurynas.uzduotis.api.dto.response;

import com.laurynas.uzduotis.api.dto.UserDTO;

import java.util.List;

public class GetAllUsersResponseDTO {
    private List<UserDTO> users;
    private String errorMessage;

    public String getErrorMessage() {
        return errorMessage;
    }

    public GetAllUsersResponseDTO() {}

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public List<UserDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserDTO> users) {
        this.users = users;
    }
}
