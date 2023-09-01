package com.flint.flint.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = {RedisUtil.class})
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() {
        String refreshToken = "2v5So4tNiKT2Lyq3x97NUcKlyuDkrW7Gr4cIkBXnCisMpgAAAYomuMJ4";
        String providerId = "1234";
    }

    @Test
    @DisplayName("레디스에 리프레쉬 토큰 저장한다")
    void save() {
        //given
        String refreshToken = "2v5So4tNiKT2Lyq3x97NUcKlyuDkrW7Gr4cIkBXnCisMpgAAAYomuMJ4";
        String providerId = "1234";
        //when
        redisUtil.save(refreshToken, providerId);

        //then
        assertThat(redisUtil.findByProviderId(providerId)).isEqualTo("2v5So4tNiKT2Lyq3x97NUcKlyuDkrW7Gr4cIkBXnCisMpgAAAYomuMJ4");
    }
}