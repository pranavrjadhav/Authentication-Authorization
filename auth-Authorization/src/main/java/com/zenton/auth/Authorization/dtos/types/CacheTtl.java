package com.zenton.auth.Authorization.dtos.types;

import lombok.Getter;

import java.time.Duration;

@Getter
public enum CacheTtl {

    USER(Duration.ofMinutes(30));

    private final Duration duration;

    CacheTtl(Duration duration) {
        this.duration = duration;
    }

}
