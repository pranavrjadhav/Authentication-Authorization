package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.config.AuthUtil;
import com.zenton.auth.Authorization.dtos.*;
import com.zenton.auth.Authorization.entity.RefreshToken;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;

    public SignupResponseDto singup(SignUpRequestDto requestDto) {
        User user = userRepository.findByUsername(requestDto.getUsername()).orElse(null);
        if (user != null) throw new IllegalArgumentException("User Already exists");

        user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .build();

        user = userRepository.save(user);
        SignupResponseDto responseDto = SignupResponseDto.builder()
                .username(user.getUsername())
                .id(user.getId())
                .build();
        return responseDto;

    }

    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getUsername(), loginRequestDto.getPassword())
        );
        User user = (User) authentication.getPrincipal();
        if (user == null) {
            return new LoginResponseDto("", "", "", "User not authenticated invalid user");
        }
        RefreshToken refreshToken = refreshTokenService.findByUserIdIfTokenExistsForTheseUser(loginRequestDto.getUsername());
        if (refreshToken == null) {
            RefreshToken refreshToken1 = refreshTokenService.createRefreshToken(loginRequestDto.getUsername());


            String token = authUtil.generateAccessToken(user);

            return new LoginResponseDto(token, user.getUsername(), refreshToken1.getToken(), "User validated successfully with new token !");
        }
        // authenticationManager ----> providerManager ---->DaoAuthenticationProvider--------->(method call) --> retrieveUser --> additionalAuthenticationChecks --> createSuccessAuthentication (return Authentication object)
        String token = authUtil.generateAccessToken(user);

        return new LoginResponseDto(token, user.getUsername(), refreshToken.getToken(), "User validated successfully same token!");
    }

    public RefreshTokenResponse refreshToken(String token) {
        RefreshToken refreshToken = refreshTokenService.findByToken(token).orElse(null);
        if (refreshToken == null) {
            return new RefreshTokenResponse("", RefreshTokenStatus.NotExists, "RefreshToken doesn't Exist, Redirect user to login page");
        }
        RefreshToken refreshToken1 = refreshTokenService.verifyExpiration(refreshToken);
        if (refreshToken1 == null) {
            return new RefreshTokenResponse("", RefreshTokenStatus.Expired, "RefreshToken is Expired, Redirect user to login page");
        }
        String jwt = authUtil.generateAccessToken(refreshToken.getUser());
        return new RefreshTokenResponse(jwt, RefreshTokenStatus.Valid, "Refresh Token is valid, Jwt token is generated ");
    }

    @Transactional
    public LogoutResponseDto logout(String token){
        RefreshToken refreshToken = refreshTokenService.findByToken(token).orElse(null);
        if(refreshToken != null){
            refreshTokenService.delete(refreshToken);
        }
        return new LogoutResponseDto(refreshToken.getUser().getUsername(),"User has been logged out successfully!");
    }

    /* JWT expired
               ↓
    Backend returns 401 Unauthorized
               ↓
    Frontend intercepts 401
               ↓
    Frontend calls /refresh endpoint
               ↓
    Browser automatically sends refresh cookie
                ↓
    Backend validates refresh token
               ↓
    Backend generates NEW JWT
               ↓
    Frontend retries original request
    */

    // logout

    /*
        Frontend calls /logout
    ↓
    Backend deletes refresh token from DB
    ↓
    Backend clears cookie
    ↓
    Frontend deletes JWT
     */

}