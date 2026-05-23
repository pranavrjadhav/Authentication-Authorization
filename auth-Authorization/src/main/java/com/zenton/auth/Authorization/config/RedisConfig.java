package com.zenton.auth.Authorization.config;

import org.hibernate.sql.Template;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/*
RedisCacheManager
    = automatic method caching

RedisTemplate
    = manual Redis programming

    docker exec -it <redis-container> redis-cli
 */
@Configuration
public class RedisConfig {
    @Bean
    public RedisTemplate<String,Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String, Object> template= new RedisTemplate<>(); //RedisTemplate: It provides methods to perform operations like saving, retrieving, and deleting data in Redis.
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(
                new GenericJackson2JsonRedisSerializer()
        );
        return template;
    }

}

/*

@Configuration  // Marks this class as a Spring configuration class
public class RedisConfig {

    @Bean  // Defines a Spring bean for RedisCacheManager
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {
        // Define cache configuration
        RedisCacheConfiguration cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10)) // Set time-to-live (TTL) for cache entries to 10 minutes
                .disableCachingNullValues() // Prevent caching of null values
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new Jackson2JsonRedisSerializer<>(ProductDto.class))); // Serialize values using Jackson JSON serializer

        // Create and return a RedisCacheManager with the specified configuration
        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(cacheConfig) // Apply default cache configuration
                .build();
    }
}

 */