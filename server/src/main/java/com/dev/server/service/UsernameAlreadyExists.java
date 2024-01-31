package com.dev.server.service;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExists extends GeneralException {
    public UsernameAlreadyExists(String username) {
        super("User with email " + username + "is already exists", HttpStatus.CONFLICT);
    }
}
