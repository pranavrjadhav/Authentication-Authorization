package com.zenton.auth.Authorization.dtos;

import lombok.Getter;


@Getter
public enum CachePrefix {

        USER("user:"),

        BLACKLIST("blacklist:"),
    ;

    private final String value;

    CachePrefix(String value) {
        this.value = value;
    }

}
