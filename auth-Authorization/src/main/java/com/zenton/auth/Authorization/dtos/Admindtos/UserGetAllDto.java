package com.zenton.auth.Authorization.dtos.Admindtos;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserGetAllDto {
    private Long id;
    private String username;
}
