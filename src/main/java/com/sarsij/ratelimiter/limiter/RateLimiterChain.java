package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;

import java.util.List;

public class RateLimiterChain implements RateLimiter{
    private final List<RateLimiter> rateLimiters;

    public RateLimiterChain(List<RateLimiter> rateLimiters){
        this.rateLimiters = rateLimiters;
    }

    @Override
    public boolean allow(RequestContext context) {
        for(RateLimiter rateLimiter:rateLimiters){
            if(!rateLimiter.allow(context)){
                return false;
            }
        }
        return true;
    }
}
