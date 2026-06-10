package com.zenton.auth.Authorization.dtos.Securitydtos;

import com.zenton.auth.Authorization.config.RolesPermissionMapping;
import com.zenton.auth.Authorization.dtos.types.RoleType;
import com.zenton.auth.Authorization.entity.Permissions;
import com.zenton.auth.Authorization.entity.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

//Security principal


@Getter
@Builder
public class AuthenticatedUser implements UserDetails {

    private Long id;
    private String username;
    private String password;
    private Set<String> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }

    /*
    here inside the method reference we are just doing these
    [
    new SimpleGrantedAuthority("ROLE_ADMIN"),
    new SimpleGrantedAuthority("car:read"),
    new SimpleGrantedAuthority("car:write")
]
     */
}
