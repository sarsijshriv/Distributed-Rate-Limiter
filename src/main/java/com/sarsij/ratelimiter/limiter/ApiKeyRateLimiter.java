package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;
import com.sarsij.ratelimiter.store.TokenBucketStore;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;

@Component
public class ApiKeyRateLimiter implements RateLimiter{

    RateLimitKeyBuilder keyBuilder;
    TokenBucketStore store;

    private final Counter allowedRequests;
    private final Counter rejectedRequests;

    @Value("${rate-limiter.api-key.capacity}")
    private int capacity;

    @Value("${rate-limiter.api-key.refill-tokens}")
    private int refillTokens;

    @Value("${rate-limiter.api-key.refill-duration-millis}")
    private long refillDurationMillis;

    public ApiKeyRateLimiter(RateLimitKeyBuilder keyBuilder, TokenBucketStore store, MeterRegistry meterRegistry){
        this.keyBuilder = keyBuilder;
        this.store = store;
        this.allowedRequests =
                Counter.builder(
                        "rate_limiter_allowed_requests"
                ).register(meterRegistry);
        this.rejectedRequests =
                Counter.builder(
                        "rate_limiter_rejected_requests"
                ).register(meterRegistry);
    }

    @Override
    public boolean allow(RequestContext context) {
        String key = keyBuilder.apiKey(context);
        boolean allowed = store.tryConsume(key, capacity, refillTokens, refillDurationMillis);
        if (allowed) {
            allowedRequests.increment();
        } else {
            rejectedRequests.increment();
        }
        return allowed;
    }
}
