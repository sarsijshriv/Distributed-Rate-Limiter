package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;

public class ApiKeyLimiterChain implements RateLimiter{

    RateLimitKeyBuilder keyBuilder;

    public ApiKeyLimiterChain(RateLimitKeyBuilder keyBuilder){
        this.keyBuilder = keyBuilder;
    }

    @Override
    public boolean allow(RequestContext context) {
        String key = keyBuilder.apiKey(context);
        return true;
    }
}
