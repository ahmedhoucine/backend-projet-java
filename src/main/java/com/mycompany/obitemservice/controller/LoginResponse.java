package com.mycompany.obitemservice.controller;

import com.mycompany.obitemservice.model.UserModel;

public class LoginResponse {
    private boolean success;
    private String message;
    private UserModel user;

    public LoginResponse(boolean success, String message, UserModel user) {
        this.success = success;
        this.message = message;
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public UserModel getUser() {
        return user;
    }
}
