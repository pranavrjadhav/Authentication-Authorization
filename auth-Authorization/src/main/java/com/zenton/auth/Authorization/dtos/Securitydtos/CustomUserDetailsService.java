package com.zenton.auth.Authorization.dtos.Securitydtos;

import com.zenton.auth.Authorization.entity.Role;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService
        implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(
            String username
    ) throws UsernameNotFoundException {

        User user = userRepository
                .findByUsernameWithRolesAndPermissions(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found"
                        ));
        Set<String> authorities = new HashSet<>();

        // roles amd permission added in authorities
        for (Role role : user.getRoles()) {

            authorities.add(
                    "ROLE_" + role.getName()
            );
            role.getPermissions()
                    .forEach(permission ->
                            authorities.add(
                                    permission.getName()
                            )
                    );
        }


        return AuthenticatedUser.builder()
                .id(user.getId())
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .build();
    }
}
