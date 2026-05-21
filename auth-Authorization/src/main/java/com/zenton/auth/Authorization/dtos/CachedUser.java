package com.zenton.auth.Authorization.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CachedUser {
    private Long id;
    private String username;
    private List<String> roles;
}
