package com.dev.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponseDTO {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}
