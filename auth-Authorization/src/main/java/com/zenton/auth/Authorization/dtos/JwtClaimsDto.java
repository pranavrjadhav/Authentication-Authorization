package com.zenton.auth.Authorization.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaimsDto {
    public String jti;
    public String username;
    public Date expirationTime;
}
