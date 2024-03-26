package com.dev.server.contoller;

import com.dev.server.dto.AuthRequestDto;
import com.dev.server.dto.AuthResponseDto;
import com.dev.server.dto.RefreshRequestDto;
import com.dev.server.dto.RegisterRequestDto;
import com.dev.server.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;

@RestController
@RequestMapping("/auth")
@Validated
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public AuthResponseDto login(@RequestBody AuthRequestDto authRequestDTO) {
        return authService.login(authRequestDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register (@RequestBody RegisterRequestDto registerRequestDTO) {
        authService.register(registerRequestDTO);
    }

    @PostMapping("/refresh")
    public AuthResponseDto refresh(@RequestBody RefreshRequestDto refreshRequestDTO) {
        return authService.refresh(refreshRequestDTO);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout (@RequestParam(required = false, defaultValue = "false") boolean full,
                        @RequestBody RefreshRequestDto refreshRequestDTO) {
        //todo: change to auth request
        if (full) {
            authService.logoutFull(refreshRequestDTO);
        }
        else {
            authService.logout(refreshRequestDTO);
        }
    }
}
