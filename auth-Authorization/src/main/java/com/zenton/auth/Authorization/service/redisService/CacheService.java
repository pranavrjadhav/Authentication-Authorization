package com.zenton.auth.Authorization.service.redisService;

import com.zenton.auth.Authorization.dtos.types.CacheType;

import java.time.Duration;

public interface CacheService {
    public <T> void save(
            CacheType type,
            String key,
            T value,
            Duration ttl
    );

    public <T> T get(
            CacheType type,
            String Key,
            Class<T> clazz
    );


}
