package com.dev.server.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class RefreshRequestDTO {
    @NotBlank
    private String refreshToken;
}
