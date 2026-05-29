package com.zenton.auth.Authorization.dtos.Securitydtos;

import com.zenton.auth.Authorization.config.RolesPermissionMapping;
import com.zenton.auth.Authorization.dtos.types.RoleType;
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
    private Set<RoleType> roles;
    private String password;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        roles.forEach(
                roleType -> {
                    Set<SimpleGrantedAuthority> permissions = RolesPermissionMapping.getAuthoritiesForRole(roleType);
                    authorities.addAll(permissions);
                    authorities.add(new SimpleGrantedAuthority("ROLE_"+roleType.name()));
                }
        );
        return authorities;
    }
}
