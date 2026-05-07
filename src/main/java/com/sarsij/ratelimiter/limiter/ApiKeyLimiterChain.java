package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;
import com.sarsij.ratelimiter.store.TokenBucketStore;
import org.apache.el.parser.Token;

public class ApiKeyLimiterChain implements RateLimiter{

    RateLimitKeyBuilder keyBuilder;
    TokenBucketStore store;

    public ApiKeyLimiterChain(RateLimitKeyBuilder keyBuilder, TokenBucketStore store){
        this.keyBuilder = keyBuilder;
        this.store = store;
    }

    @Override
    public boolean allow(RequestContext context) {
        String key = keyBuilder.apiKey(context);
        return store.tryConsume(key, 10, 10, 60000);
    }
}
