package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.Authdtos.JwtClaimsDto;
import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
import com.zenton.auth.Authorization.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

@Component
public class AuthUtil {

    //on browser time standard is GMT our local is IST
    //Add 5:30 to GMT → IST
    //Subtract 5:30 from IST → GMT

    @Value("${jwt.secretKey}")
    private String jwtSecrectKey;

    private SecretKey getSecrectKey(){
        return Keys.hmacShaKeyFor(jwtSecrectKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateAccessToken(AuthenticatedUser user){
        return Jwts.builder()
                .id(UUID.randomUUID().toString()) // jti
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000*60*10))
                .signWith(getSecrectKey())
                .compact();
    }
    /*
    we are not setting roles and permisisin in jwt but here is the sample to set in claim
    public String generateAccessToken(AuthenticatedUser user){

    return Jwts.builder()
            .id(UUID.randomUUID().toString())
            .subject(user.getUsername())
            .claim("userId", user.getId())
            .claim("authorities", user.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 10))
            .signWith(getSecrectKey())
            .compact();
}
     */

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

    public JwtClaimsDto getUserClaim(String token){
        Claims claims = Jwts.parser()
                .verifyWith(getSecrectKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return  JwtClaimsDto.builder()
                .jti(claims.getId())
                .username(claims.getSubject())
                .expirationTime(claims.getExpiration())
                .build();
    }

}
