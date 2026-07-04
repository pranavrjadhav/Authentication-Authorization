package com.zenton.auth.Authorization.dtos.Authdtos;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String username;
    @Email(message = "Invalid email format")
    private String email;
    private String password;

//    private Set<RoleType> roles = new HashSet<>();
}
