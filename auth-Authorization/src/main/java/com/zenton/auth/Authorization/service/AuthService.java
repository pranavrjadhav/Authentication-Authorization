package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.config.AuthUtil;
import com.zenton.auth.Authorization.config.SecurityConfigUtil;
import com.zenton.auth.Authorization.dtos.Authdtos.*;
import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
import com.zenton.auth.Authorization.dtos.types.CacheType;
import com.zenton.auth.Authorization.dtos.types.RefreshTokenStatus;
import com.zenton.auth.Authorization.dtos.types.RoleType;
import com.zenton.auth.Authorization.entity.RefreshToken;
import com.zenton.auth.Authorization.entity.Role;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.RoleRepository;
import com.zenton.auth.Authorization.repository.UserRepository;
import com.zenton.auth.Authorization.service.redisService.CacheService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final RefreshTokenService refreshTokenService;
    private final CacheService redisCacheService;
    private final SecurityConfigUtil securityConfigUtil;
    private final RoleRepository roleRepository;

    public SignupResponseDto singup(SignUpRequestDto requestDto) {
        User user = userRepository.findByUsernameWithRolesAndPermissions(requestDto.getUsername()).orElse(null);
        if (user != null) throw new IllegalArgumentException("User Already exists");

        Set<Role> role = Collections.singleton(roleRepository.findByName(RoleType.USER.name()).orElseThrow());

        user = User.builder()
                .username(requestDto.getUsername())
                .password(passwordEncoder.encode(requestDto.getPassword()))
                .email(requestDto.getEmail())
                .roles(role)
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
        AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) authentication.getPrincipal();
        if (authenticatedUser == null) {
            return new LoginResponseDto("", "", "", "User not authenticated invalid user");
        }
        RefreshToken refreshToken = refreshTokenService.findByUserIdIfTokenExistsForTheseUser(authenticatedUser);
        if (refreshToken == null) {
            RefreshToken refreshToken1 = refreshTokenService.createRefreshToken(authenticatedUser);


            String token = authUtil.generateAccessToken(authenticatedUser);

            return new LoginResponseDto(token, authenticatedUser.getUsername(), refreshToken1.getToken(), "User validated successfully with new token !");
        }
        // authenticationManager ----> providerManager ---->DaoAuthenticationProvider--------->(method call) --> retrieveUser --> additionalAuthenticationChecks --> createSuccessAuthentication (return Authentication object)
        String token = authUtil.generateAccessToken(authenticatedUser);

        return new LoginResponseDto(token, authenticatedUser.getUsername(), refreshToken.getToken(), "User validated successfully same token!");
    }

    /*AuthenticationManager.authenticate()
            ↓
    ProviderManager
            ↓
    DaoAuthenticationProvider
            ↓
    UserDetailsService.loadUserByUsername()
            ↓
    userRepository.findByUsername()
            ↓
    returns UserDetails
            ↓
    PasswordEncoder.matches(raw, encoded)
            ↓
    if valid:
        authenticated Authentication object returned

     */

    public RefreshTokenResponse refreshToken(String token) {
        AuthenticatedUser authenticatedUser = securityConfigUtil.getCurrentUser();
        RefreshToken refreshToken = refreshTokenService.findByToken(token).orElse(null);
        if (refreshToken == null) {
            return new RefreshTokenResponse("", RefreshTokenStatus.NotExists, "RefreshToken doesn't Exist, Redirect user to login page");
        }
        RefreshToken refreshToken1 = refreshTokenService.verifyExpiration(refreshToken);
        if (refreshToken1 == null) {
            return new RefreshTokenResponse("", RefreshTokenStatus.Expired, "RefreshToken is Expired, Redirect user to login page");
        }
        String jwt = authUtil.generateAccessToken(authenticatedUser);
        return new RefreshTokenResponse(jwt, RefreshTokenStatus.Valid, "Refresh Token is valid, Jwt token is generated ");
    }

    @Transactional
    public LogoutResponseDto logout(String token,String authHeader){
        AuthenticatedUser authenticatedUser = securityConfigUtil.getCurrentUser();
        RefreshToken refreshToken = refreshTokenService.findByToken(token).orElse(null);
        String jwtToken = authHeader.split("Bearer ")[1];
        JwtClaimsDto jwtClaimsDto = authUtil.getUserClaim(jwtToken);

        if(refreshToken != null){
            long remainingMillis = jwtClaimsDto.getExpirationTime().getTime()-System.currentTimeMillis();
            redisCacheService.save(CacheType.blackListedJwt,jwtClaimsDto.getJti(),"revoked",Duration.ofMillis(remainingMillis));
            refreshTokenService.delete(refreshToken);
        }
        return new LogoutResponseDto(authenticatedUser.getUsername(),"User has been logged out successfully!");
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