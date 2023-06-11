package com.laurynas.uzduotis.api.dto.response;

public class SimpleResponseDTO {
    private String successMessage;
    private String errorMessage;

    public SimpleResponseDTO() {
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }
}
