package com.zenton.auth.Authorization.service.redisService;

import com.zenton.auth.Authorization.dtos.types.CacheType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        name = "redis.enabled",
        havingValue = "true"
)
public class RedisCacheService implements CacheService{

    private final RedisTemplate<String,Object> redisTemplate;

    public void delete(CacheType type,String key){
        redisTemplate.delete(type.name()+":"+key);
    }


    @Override
    public <T> void save(
            CacheType type,
            String key,
            T value,
            Duration ttl
    ){
        String redisKey = type.name()+":"+key;
        redisTemplate.opsForValue().set(
                redisKey,
                value,
                ttl
        );
    }

    @Override
    public <T> T get(
            CacheType type,
            String Key,
            Class<T> clazz
    ){
        String redisKey = type.name()+":"+Key;
        Object value = redisTemplate.opsForValue().get(redisKey);
        if(value == null) return null;
        return clazz.cast(value);
    }

}
