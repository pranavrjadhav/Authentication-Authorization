package com.zenton.auth.Authorization.repository;

import com.zenton.auth.Authorization.entity.User;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    @Query("""
          SELECT DISTINCT u
                  FROM User u
                  LEFT JOIN FETCH u.roles r
                  LEFT JOIN FETCH r.permissions
                  WHERE u.username = :username
""")
    Optional<User> findByUsernameWithRolesAndPermissions(@Param("username") String username);


    Page<User> findAll(Pageable pageable);

    Page<User> findByUsernameContainingIgnoreCase(
            String username,
            Pageable pageable
    );

}
