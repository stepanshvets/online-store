package com.dev.server.dto;

import com.dev.server.util.validation.Password;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class RegisterRequestDto {
    @NotBlank
    @Email
    private String email;


    @Size(min = 8)
    @Password
    private String password;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;
}
