package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;
import com.sarsij.ratelimiter.store.TokenBucketStore;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

@Component
public class ApiKeyRateLimiter implements RateLimiter{

    RateLimitKeyBuilder keyBuilder;
    TokenBucketStore store;

    @Value("${rate-limiter.api-key.capacity}")
    private int capacity;

    @Value("${rate-limiter.api-key.refill-tokens}")
    private int refillTokens;

    @Value("${rate-limiter.api-key.refill-duration-millis}")
    private long refillDurationMillis;

    public ApiKeyRateLimiter(RateLimitKeyBuilder keyBuilder, TokenBucketStore store){
        this.keyBuilder = keyBuilder;
        this.store = store;
    }

    @Override
    public boolean allow(RequestContext context) {
        System.out.println(capacity);
        String key = keyBuilder.apiKey(context);
        return store.tryConsume(key, capacity, refillTokens, refillDurationMillis);
    }
}
