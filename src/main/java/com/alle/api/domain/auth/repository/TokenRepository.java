package com.alle.api.domain.auth.repository;

import com.alle.api.domain.auth.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByLoginId(String loginId);
    Optional<Token> findByRefreshToken(String refreshToken);
}