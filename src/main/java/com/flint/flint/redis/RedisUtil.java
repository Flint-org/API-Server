package com.flint.flint.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.TimeUnit;

/**
 * redis CRUD 메소드
 *
 * @Author 정순원
 * @Since 2023-08-19
 */
@Repository
public class RedisUtil {

    private RedisTemplate redisTemplate;

    public RedisUtil(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //리프레쉬토큰 관련 redis 명령어
    public void save(String key, String value, Long expiration) {
        redisTemplate.opsForValue().set(key, value, expiration, TimeUnit.MILLISECONDS);
    }
    public void update(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    public void deleteByKey(String key) {
        redisTemplate.delete(String.valueOf(key));
    }
    public Object findByKey(String key) {
        return redisTemplate.opsForValue().get(key).toString();
    }

    //이메일 APICall 관련 redis 명령어
    public void saveAPICall(Long key, Integer apicall, Long expiration) {
        redisTemplate.opsForValue().set("APICall"+String.valueOf(key), apicall, expiration, TimeUnit.MILLISECONDS);
    }
    public void updateAPICall(Long key, Integer apicall) {
        redisTemplate.opsForValue().set("APICall"+String.valueOf(key), apicall);
    }
    public Integer findAPICallByKey(Long key) {
        return (Integer) redisTemplate.opsForValue().get("APICall"+String.valueOf(key));
    }

    //이메일 emailAuthNumber 관련 redis 명령어
    public void saveAuthNumber(Long key, int emailAuthNumber, Long expiration) {
        redisTemplate.opsForValue().set("AuthNumber"+String.valueOf(key), emailAuthNumber, expiration, TimeUnit.MILLISECONDS);
    }
    public void updateAuthNumber(Long key, int emailAuthNumber) {
        redisTemplate.opsForValue().set("AuthNumber"+String.valueOf(key), emailAuthNumber);
    }
    public int findEmailAuthNumberByKey(Long key) {
        return (Integer)redisTemplate.opsForValue().get("AuthNumber"+String.valueOf(key));
    }


}