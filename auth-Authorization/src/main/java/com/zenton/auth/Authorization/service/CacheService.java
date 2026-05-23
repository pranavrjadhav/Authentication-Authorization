package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.CacheType;
import com.zenton.auth.Authorization.dtos.CachedUser;
import com.zenton.auth.Authorization.dtos.JwtClaimsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    public void delete(CacheType type,String key){
        redisTemplate.delete(type.name()+":"+key);
    }


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
