package com.zenton.auth.Authorization.repository;

import com.zenton.auth.Authorization.entity.RefreshToken;
import com.zenton.auth.Authorization.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Integer> {
        Optional<RefreshToken> findByToken(String token);
        Optional<RefreshToken> findByUser(User user);
    }