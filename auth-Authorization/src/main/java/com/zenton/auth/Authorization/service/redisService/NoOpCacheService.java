package com.zenton.auth.Authorization.service.redisService;

import com.zenton.auth.Authorization.dtos.types.CacheType;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@ConditionalOnProperty(
        name = "redis.enabled",
        havingValue = "false",
        matchIfMissing = true
)
//or no property at all, you get the fallback implementation.default if redis.enabled value is missing we get these redis implementation
public class NoOpCacheService implements CacheService {
    @Override
    public <T> void save(CacheType type, String key, T value, Duration ttl) {

    }{
        //do nothing
    }

    @Override
    public <T> T get(CacheType type, String Key, Class<T> clazz) {
        return null;
    }
}
