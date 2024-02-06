package com.dev.server.contoller;

import com.dev.server.dto.AuthRequestDTO;
import com.dev.server.dto.AuthResponseDTO;
import com.dev.server.dto.RefreshRequestDTO;
import com.dev.server.dto.RegisterRequestDTO;
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

    @GetMapping("/req")
    public String req(@RequestParam(required = false) @Email String email) {
        return email;
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@RequestBody AuthRequestDTO authRequestDTO) {
        return authService.login(authRequestDTO);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void register (@RequestBody RegisterRequestDTO registerRequestDTO) {
        authService.register(registerRequestDTO);
    }

    @PostMapping("/refresh")
    public AuthResponseDTO refresh(@RequestBody RefreshRequestDTO refreshRequestDTO) {
        return authService.refresh(refreshRequestDTO);
    }

    @PostMapping("/logout")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout (@RequestParam(required = false, defaultValue = "false") boolean full,
                        @RequestBody RefreshRequestDTO refreshRequestDTO) {
        //todo: change to auth request
        if (full) {
            authService.logoutFull(refreshRequestDTO);
        }
        else {
            authService.logout(refreshRequestDTO);
        }
    }
}
