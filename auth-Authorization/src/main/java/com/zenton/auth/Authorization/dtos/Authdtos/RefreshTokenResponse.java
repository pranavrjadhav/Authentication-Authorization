package com.zenton.auth.Authorization.dtos.Authdtos;

import com.zenton.auth.Authorization.dtos.types.RefreshTokenStatus;
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
