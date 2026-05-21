package com.zenton.auth.Authorization.service;

import com.zenton.auth.Authorization.dtos.CacheType;
import com.zenton.auth.Authorization.dtos.CachedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String,Object> redisTemplate;

    private static final String PREFIX = CacheType.user+":";

    public CachedUser get(String key){
        return (CachedUser)
                redisTemplate.opsForValue().get(PREFIX+key);
    }

    public void save(CachedUser user){
        redisTemplate.opsForValue().set(
                PREFIX+user.getUsername(),
                user,
                Duration.ofMinutes(5)
        );
    }

    public void delete(String username){
        redisTemplate.delete(PREFIX+username);
    }

}
