package com.zenton.auth.Authorization.dtos.types;

import lombok.Getter;

@Getter
public enum PermissionType {
    Read("read"),  //admin
    Write("write"),
    Delete("delete"),
    All_Access("allAccess");



    private final String value;

    PermissionType(String value) {
        this.value = value;
    }
}
