package com.sarsij.ratelimiter.store;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class RedisTokenBucketStore implements TokenBucketStore{

    StringRedisTemplate redisTemplate;

    public RedisTokenBucketStore(StringRedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean tryConsume(String key, int capacity, int refillTokens, long refillDurationMillis) {
        String value = redisTemplate.opsForValue().get(key);
        int tokens;
        if(value == null){
            tokens = capacity;
        } else {
            tokens = Integer.parseInt(value);
        }
        if(tokens<=0) return false;
        redisTemplate.opsForValue().set(key, String.valueOf(tokens - 1));
        return true;
    }
}
