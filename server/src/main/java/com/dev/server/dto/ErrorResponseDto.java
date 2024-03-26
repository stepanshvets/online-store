package com.dev.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ErrorResponseDto {
    private Date timestamp;
    private Integer status;
    private String error;
    private String path;
}
