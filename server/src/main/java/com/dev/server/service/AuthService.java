package com.dev.server.service;

import com.dev.server.dto.AuthRequestDTO;
import com.dev.server.dto.AuthResponseDTO;
import com.dev.server.dto.RefreshRequestDTO;
import com.dev.server.dto.RegisterRequestDTO;
import com.dev.server.model.Role;
import com.dev.server.model.Token;
import com.dev.server.model.User;
import com.dev.server.repository.TokenRepository;
import com.dev.server.repository.UserRepository;
import com.dev.server.security.JwtUtil;
import com.dev.server.security.UserDetailsImpl;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public AuthResponseDTO login(AuthRequestDTO authRequestDTO) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    authRequestDTO.getEmail(), authRequestDTO.getPassword()));

            User user = userRepository.findByEmail(authRequestDTO.getEmail()).orElseThrow(
                    () -> new UsernameNotFoundException("User is not found"));
            UserDetails userDetails = new UserDetailsImpl(user);

            String accessToken = jwtUtil.generateAccessToken(userDetails);

            String refreshToken = jwtUtil.generateRefreshToken(userDetails);
            Token token = new Token(refreshToken, user);
            tokenRepository.save(token);
            return new AuthResponseDTO(accessToken, refreshToken);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new GeneralException("Not valid email or password", HttpStatus.UNAUTHORIZED);
        }
    }

    public AuthResponseDTO refresh(RefreshRequestDTO refreshRequestDTO) {
        UserDetails userDetails = jwtUtil.parseRefreshToken(refreshRequestDTO.getRefreshToken());
        if (userDetails == null) {
            throw new GeneralException("JWT refresh is not valid", HttpStatus.UNAUTHORIZED);
        }

        String accessToken = jwtUtil.generateAccessToken(userDetails);

        Token token = tokenRepository.findByRefreshToken(refreshRequestDTO.getRefreshToken())
                .orElseThrow(() -> new GeneralException("JWT refresh is not valid", HttpStatus.UNAUTHORIZED));
        String newRefreshToken =  jwtUtil.generateRefreshToken(userDetails);
        token.setRefreshToken(newRefreshToken);
        tokenRepository.save(token);

        return new AuthResponseDTO(accessToken, newRefreshToken);
    }


    public void register(RegisterRequestDTO registerRequestDTO) {
        if (userRepository.findByEmail(registerRequestDTO.getEmail()).isPresent())
            throw new UsernameAlreadyExists(registerRequestDTO.getEmail());

        User user = new User();
        user.setEmail(registerRequestDTO.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequestDTO.getPassword()));
        user.setFirstName(registerRequestDTO.getFirstName());
        user.setLastName(registerRequestDTO.getLastName());
        user.setRole(Role.USER);

        userRepository.save(user);
    }

    @Transactional
    public void logout(RefreshRequestDTO refreshRequestDTO) {
        if (tokenRepository.deleteByRefreshToken(
                refreshRequestDTO.getRefreshToken()).isPresent()) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }

    @Transactional
    public void logoutFull(RefreshRequestDTO refreshRequestDTO) {
        Token token = tokenRepository.findByRefreshToken(refreshRequestDTO.getRefreshToken()).orElse(null);
        if (token != null && tokenRepository.deleteByUser(token.getUser()) > 0) {
            SecurityContextHolder.getContext().setAuthentication(null);
        }
    }
}
