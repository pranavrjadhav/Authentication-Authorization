package com.zenton.auth.Authorization.dtos.Authdtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String username;
    private String password;

//    private Set<RoleType> roles = new HashSet<>();
}
