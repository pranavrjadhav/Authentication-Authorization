package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.types.PermissionType;
import com.zenton.auth.Authorization.dtos.types.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zenton.auth.Authorization.dtos.types.PermissionType.*;
import static com.zenton.auth.Authorization.dtos.types.RoleType.ADMIN;
import static com.zenton.auth.Authorization.dtos.types.RoleType.USER;

public class RolesPermissionMapping {

    private static final Map<RoleType, Set<PermissionType>> map = Map.of(
            ADMIN,Set.of(ADMIN_MANAGE,ADMIN_WRITE,ADMIN_READ),
            USER,Set.of(USER_READ)
    );

    public  static Set<SimpleGrantedAuthority> getAuthoritiesForRole(RoleType role){
        return map.get(role).stream()
                .map(permissionType -> new SimpleGrantedAuthority(permissionType.getValue()))
                .collect(Collectors.toSet());
    }

}
