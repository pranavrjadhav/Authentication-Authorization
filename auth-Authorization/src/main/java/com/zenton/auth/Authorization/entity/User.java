package com.zenton.auth.Authorization.entity;

import com.zenton.auth.Authorization.dtos.types.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@Table(name="app_user")
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String username;
    @Column(unique = true,nullable = false)
    private String password;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)  //create separate table only for these [String,Enum,Integer] not for entities we use onetomany relationship there
    @Enumerated(EnumType.STRING) //store enum as strings else without these they store enum as number
    Set<RoleType> roles = new HashSet<>();



}
