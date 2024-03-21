package com.dev.server.repository;

import com.dev.server.model.Token;
import com.dev.server.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByRefreshToken(String refreshToken);
    Optional<Token> deleteByRefreshToken(String refreshToken);
    Long deleteByUser(User user);
}
