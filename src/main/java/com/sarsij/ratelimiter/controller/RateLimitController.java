package com.sarsij.ratelimiter.controller;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.context.RequestContextFactory;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;
import com.sarsij.ratelimiter.limiter.ApiKeyLimiterChain;
import com.sarsij.ratelimiter.limiter.RateLimiter;
import com.sarsij.ratelimiter.limiter.RateLimiterChain;
import com.sarsij.ratelimiter.store.TokenBucketStore;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RateLimitController {
    private final RequestContextFactory requestContextFactory;
    private final RateLimiterChain rateLimiter;
    private final TokenBucketStore store;

    public RateLimitController(RequestContextFactory requestContextFactory, RateLimitKeyBuilder keyBuilder, TokenBucketStore store){
        this.requestContextFactory = requestContextFactory;
        this.rateLimiter = new RateLimiterChain(List.of(new ApiKeyLimiterChain(keyBuilder, store)));
        this.store = store;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        RequestContext context = requestContextFactory.fromHttpRequest(request);
        boolean allowed = rateLimiter.allow(context);
        return allowed ? "ALLOW" : "REJECT";
    }
}
