package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.Admindtos.ListOfRolesAndPermission;
import com.zenton.auth.Authorization.dtos.Admindtos.UserGetAllDto;
import com.zenton.auth.Authorization.dtos.Admindtos.UserGetByIdDto;
import com.zenton.auth.Authorization.entity.Permissions;
import com.zenton.auth.Authorization.entity.Role;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final UserRepository userRepository;

    public Page<UserGetAllDto> getAllUsers(
            int page,
            int size,
            String username
    ){
        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );
        Page<User> users;
        if(username == null || username.isBlank()){
            users = userRepository.findAll(pageable);
        }else{
            users = userRepository.findByUsernameContainingIgnoreCase(
                    username,
                    pageable
            );
        }

        return users.map(user ->
                new UserGetAllDto(user.getId(), user.getUsername())
                );

    }


    public UserGetByIdDto getUserById(Long userid) {
          User user = userRepository.findByUserIdWithRolesAndPermissions(userid).orElseThrow( () -> new UsernameNotFoundException(
                  "User not found with id: " + userid
          ));
          Set<String> roles = user.getRoles()
                  .stream()
                  .map(Role::getName)
                  .collect(Collectors.toSet());

          Set<String> permissions = user.getRoles()
                  .stream()
                  .flatMap(role -> role.getPermissions().stream())
                  .map(Permissions::getName)
                  .collect(Collectors.toSet());

          return UserGetByIdDto.builder()
                  .id(user.getId())
                  .username(user.getUsername())
                  .roles(roles)
                  .permissions(permissions)
                  .createdAt(user.getCreatedAt())
                  .updatedAt(user.getUpdatedAt())
                  .build();

    }

//    public ListOfRolesAndPermission getAllRolesPermission() {
//
//    }
}
