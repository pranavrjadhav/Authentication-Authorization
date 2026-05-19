package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class AuthUtil {


    @Value("${jwt.secretKey}")
    private String jwtSecrectKey;

    private SecretKey getSecrectKey(){
        return Keys.hmacShaKeyFor(jwtSecrectKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(User user){
        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*2))
                .signWith(getSecrectKey())
                .compact();
    }

    // epirydate calculation  | Unit     | Value             |
    //| -------- | ----------------- |
    //| 1 second | 1000 milliseconds |
    //| 1 minute | 60 seconds        |
    //| 1 hour   | 60 minutes        |
    //| 1 day    | 24 hours          |

    //1000 milliseconds
    //= 1 second

    //1 second * 60
    //= 60 seconds
    //= 1 minute

    //1 minute * 10
    //= 10 minutes

    //1000 * 60 * 60 * 24 = 24 hr

    //1000 * 60 * 60 * 24 * 7 = 7 days

    public String getUsernameFromToken(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecrectKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims.getSubject();
    }

}
