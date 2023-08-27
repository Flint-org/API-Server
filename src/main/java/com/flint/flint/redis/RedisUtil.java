package com.flint.flint.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * redis CRUD 메소드
 * @Author 정순원
 * @Since 2023-08-19
 */
@Repository
public class RedisUtil {

    private RedisTemplate redisTemplate;

    @Value("${jwt.refreshTokenExpiration}")
    private long refreshTokenExpiration;


    public RedisUtil(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void save(String refreshToken, String providerId) {
        redisTemplate.opsForValue().set(providerId, refreshToken, refreshTokenExpiration, TimeUnit.MILLISECONDS);
    }

    public void deleteById(String providerId) {
        redisTemplate.delete(String.valueOf(providerId));
    }

    public String findByProviderId(final String providerId) {
        return redisTemplate.opsForValue().get(providerId).toString();
    }
}