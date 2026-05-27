package com.zenton.auth.Authorization.dtos;

import lombok.Getter;

@Getter
public enum PermissionType {
    ADMIN_MANAGE("user:manage"),  //admin
    USER_READ("user:read"),
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write");


    private final String value;

    PermissionType(String value) {
        this.value = value;
    }
}
