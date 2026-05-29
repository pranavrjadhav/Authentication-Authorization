package com.zenton.auth.Authorization.dtos.Cachedtos;

import com.zenton.auth.Authorization.dtos.types.RoleType;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedUser {
    private Long id;
    private String username;
    private Set<String> roles;
}
