package com.zenton.auth.Authorization.controller;

import com.zenton.auth.Authorization.dtos.*;
import com.zenton.auth.Authorization.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    @Value("${refreshToken}")
    private String refreshTokenExpiryDate;
    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDto> signup(@RequestBody SignUpRequestDto requestDto){
        return  ResponseEntity.ok(authService.singup(requestDto));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {
       // return ResponseEntity.ok(authService.login(loginRequestDto));
        LoginResponseDto loginResponseDto = authService.login(loginRequestDto);
        Cookie refreshCookie = new Cookie("refreshToken",
                loginResponseDto.getRefreshToken()
                );
        refreshCookie.setHttpOnly(true);
        //refreshCookie.setSecure(true);  //for prod in https only
        refreshCookie.setSecure(false);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(Integer.parseInt(refreshTokenExpiryDate));
        response.addCookie(refreshCookie);
        loginResponseDto.setRefreshToken(null);
        return ResponseEntity.ok(loginResponseDto);


    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshTokenResponse> refreshToken(@CookieValue("refreshToken")
                                                             String refreshToken){
              return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<LogoutResponseDto> logout(@CookieValue("refreshToken")
                                                   String refreshToken){
        return ResponseEntity.ok(authService.logout(refreshToken));
    }

}
