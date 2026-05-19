package com.zenton.auth.Authorization.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshTokenResponse {
    public String jwtToken;
    public RefreshTokenStatus refreshTokenStatus;
    public String message;
}
