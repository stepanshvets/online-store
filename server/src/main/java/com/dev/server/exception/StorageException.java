package com.dev.server.exception;

import org.springframework.http.HttpStatus;

public class StorageException extends GeneralException {
    public StorageException(String message, HttpStatus httpStatus) {
        super(message, httpStatus);
    }

    public StorageException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
