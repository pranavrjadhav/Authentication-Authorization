package com.zenton.auth.Authorization.config;

import com.zenton.auth.Authorization.dtos.Securitydtos.AuthenticatedUser;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityConfigUtil {

    public AuthenticatedUser getCurrentUser() {

        return (AuthenticatedUser)
                SecurityContextHolder
                        .getContext()
                        .getAuthentication()
                        .getPrincipal();
    }
}