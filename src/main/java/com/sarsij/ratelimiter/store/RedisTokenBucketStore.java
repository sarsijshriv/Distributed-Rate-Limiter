package com.sarsij.ratelimiter.store;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RedisTokenBucketStore implements TokenBucketStore {

    StringRedisTemplate redisTemplate;
    private final DefaultRedisScript<Long> consumeScript;

    public RedisTokenBucketStore(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.consumeScript = new DefaultRedisScript<>();
        this.consumeScript.setLocation(
                new ClassPathResource(
                        "scripts/tokenBucket.lua"
                )
        );
        this.consumeScript.setResultType(Long.class);
    }

    @Override
    public boolean tryConsume(
            String key,
            int capacity,
            int refillTokens,
            long refillDurationMillis) {

        Long result = redisTemplate.execute(
                consumeScript,
                List.of(key),
                String.valueOf(capacity),
                String.valueOf(refillTokens),
                String.valueOf(refillDurationMillis),
                String.valueOf(System.currentTimeMillis())
        );

        return result != null && result == 1L;
    }
}
