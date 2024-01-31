package com.dev.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponseDTO {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}
