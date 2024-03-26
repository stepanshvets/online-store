package com.dev.server.service;

import com.dev.server.model.User;
import com.dev.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getAuthUser() {
        Authentication authentication =  SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new BadCredentialsException("Unauthorized");
        }

        String email = authentication.getPrincipal().toString();

        return userRepository.findByEmail(email).orElseThrow(() -> new BadCredentialsException("User is not found"));
    }
}
