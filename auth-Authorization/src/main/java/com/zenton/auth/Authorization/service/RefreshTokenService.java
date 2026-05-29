package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
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

    public RefreshToken createRefreshToken(AuthenticatedUser authenticatedUser){
        RefreshToken refreshToken = RefreshToken.builder()
                .userId(authenticatedUser.getId())
                .token(UUID.randomUUID().toString())
                .expiryDate(new Date(System.currentTimeMillis() + 1000*60*15))
                .build();
        return  refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken findByUserIdIfTokenExistsForTheseUser(AuthenticatedUser user){
        if (user == null) {
            return null;
        }
        RefreshToken refreshToken =
                refreshTokenRepository.findByUserId(user.getId())
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

    public void delete(RefreshToken refreshToken) {

        refreshTokenRepository.delete(refreshToken);
    }

}
