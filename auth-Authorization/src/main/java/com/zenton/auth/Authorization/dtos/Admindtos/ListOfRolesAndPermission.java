package com.zenton.auth.Authorization.dtos.Admindtos;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ListOfRolesAndPermission {

    private List<RoleDto> roles;
    private List<PermissionDto> permissions;
}
