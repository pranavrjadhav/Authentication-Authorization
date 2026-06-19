package com.zenton.auth.Authorization.dtos                                                                                                                                                                                                                                                                                                                              .Admindtos;

import com.zenton.auth.Authorization.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGetByIdDto {

    private Long id;
    private String username;
    private Set<String> roles;
    private Set<String> permissions;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;


}
