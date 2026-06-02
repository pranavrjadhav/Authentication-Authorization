package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.Admindtos.UserGetAllDto;
import com.zenton.auth.Authorization.entity.User;
import com.zenton.auth.Authorization.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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


}
