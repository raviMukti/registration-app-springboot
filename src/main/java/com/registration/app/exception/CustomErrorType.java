package com.registration.app.exception;

import com.registration.app.dto.UserDTO;

public class CustomErrorType extends UserDTO {
    private String errorMessage;

    public CustomErrorType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage(){
        return errorMessage;
    }
}
