package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.PermissionType;
import com.zenton.auth.Authorization.dtos.RoleType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zenton.auth.Authorization.dtos.PermissionType.*;
import static com.zenton.auth.Authorization.dtos.RoleType.ADMIN;
import static com.zenton.auth.Authorization.dtos.RoleType.USER;

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
