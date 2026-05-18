package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.entity.RefreshToken;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.RefreshTokenRepository;
import com.zenton.auth.Authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {


//    @Value("${refreshToken}")
//    private String refreshTokenExpiryDate;


    private  final RefreshTokenRepository refreshTokenRepository;
    private  final UserRepository userRepository;

    public RefreshToken createRefreshToken(String username){
        RefreshToken refreshToken = RefreshToken.builder()
                .user(userRepository.findByUsername(username).orElseThrow())
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis() + 1000*60*5))
                .build();
        return  refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken findByUserIdIfTokenExistsForTheseUser(String username){
        User user = userRepository.findByUsername(username)
                .orElse(null);

        if (user == null) {
            return null;
        }
        RefreshToken refreshToken =
                refreshTokenRepository.findByUser(user)
                        .orElse(null);
        if(refreshToken != null){
            return verifyExpiration(refreshToken);
        }
        return null;
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(new Date(System.currentTimeMillis())) < 0) {
            refreshTokenRepository.delete(token);
            return null;
        }
        return token;
    }

}
