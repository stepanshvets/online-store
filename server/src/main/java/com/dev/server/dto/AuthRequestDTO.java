package com.dev.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthRequestDTO {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
