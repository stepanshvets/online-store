package com.dev.server.dto;

import com.dev.server.util.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class AuthRequestDto {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;
}
