package com.zenton.auth.Authorization.entity;

import com.zenton.auth.Authorization.config.RolesPermissionMapping;
import com.zenton.auth.Authorization.dtos.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.TypeBinderType;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name="app_user")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)  //create separate table only for these [String,Enum,Integer] not for entities we use onetomany relationship there
    @Enumerated(EnumType.STRING) //store enum as strings else without these they store enum as number
    Set<RoleType> roles = new HashSet<>();

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
