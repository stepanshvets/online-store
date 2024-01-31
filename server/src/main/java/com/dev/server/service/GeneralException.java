package com.dev.server.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class GeneralException extends RuntimeException {
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;

    public GeneralException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }

    public GeneralException(String message, HttpStatus httpStatus, LocalDateTime timestamp) {
        super(message);
        this.httpStatus = httpStatus;
        this.timestamp = timestamp;
    }
}
