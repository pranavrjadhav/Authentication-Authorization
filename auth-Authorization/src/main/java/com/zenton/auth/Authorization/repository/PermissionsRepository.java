package com.zenton.auth.Authorization.repository;

import com.zenton.auth.Authorization.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;

import java.security.Permission;
import java.util.Optional;

public interface PermissionsRepository extends JpaRepository<Permissions,Long> {
    boolean existsByName(String name);

    Optional<Permissions> findByName(String name);
}
