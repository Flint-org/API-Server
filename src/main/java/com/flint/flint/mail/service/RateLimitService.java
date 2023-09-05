package com.flint.flint.mail.service;

import com.flint.flint.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 이메일 인증번호 발송 API호출 횟수 제한 서비스
 *
 * @Author 정순원
 * @Since 2023-08-31
 */
@Service
@RequiredArgsConstructor
public class RateLimitService {

    private final static int MAX_API_CALL = 10;
    private final static Long Expiration = 1800000L; //30분
    private final RedisUtil redisUtil;

    public Boolean checkAPICall(Long key) {
        String apiCall = String.valueOf(redisUtil.findAPICallByKey(key));
        if (apiCall == null) { //한 번도 호출 안 한 경우
            redisUtil.saveAPICall(key, String.valueOf((int)1), Expiration);
            return true;
        } else if (Integer.parseInt(apiCall) < MAX_API_CALL) { //10 번 미만 호출 한 경우
            redisUtil.updateAPICall(key, String.valueOf(Integer.parseInt(apiCall) + 1));
            return true;
        }
        return false; //10 번인 경우
    }
}