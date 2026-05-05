package com.sarsij.ratelimiter.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTestService {

    public final StringRedisTemplate redisTemplate;

    RedisTestService(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public String testRedis(){
    redisTemplate.opsForValue().set("test-key","Hello-redis-test");
    return redisTemplate.opsForValue().get("test-key");
    }
}
