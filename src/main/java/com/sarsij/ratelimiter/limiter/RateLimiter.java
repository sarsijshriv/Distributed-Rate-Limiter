package com.sarsij.ratelimiter.limiter;

import com.sarsij.ratelimiter.context.RequestContext;

public interface RateLimiter {
    boolean allow(RequestContext context);
}
