package com.zenton.auth.Authorization.dtos.types;

import lombok.Getter;

@Getter
public enum ResourceType {
    CarRegistry("carRegistry");

    private final String value;

    ResourceType(String value) {
        this.value = value;
    }
}
