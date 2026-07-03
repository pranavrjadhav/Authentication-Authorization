package com.zenton.auth.Authorization.entity;

import com.zenton.auth.Authorization.dtos.types.RoleType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
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
    @Column(unique = true,nullable = false)
    private  String email;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    private LocalDateTime updatedAt;

}

// add @Column(unique = true, nullable = false)
//private String userCode;

//user.setUserCode(
//        "USR_" + UUID.randomUUID()
//);

// examaple :-- USR_550e8400-e29b-41d4-a716-446655440000   /  expose :--- USR_8f4c2a7b