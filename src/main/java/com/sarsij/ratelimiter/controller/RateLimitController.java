package com.sarsij.ratelimiter.controller;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.context.RequestContextFactory;
import com.sarsij.ratelimiter.limiter.ApiKeyRateLimiter;
import com.sarsij.ratelimiter.limiter.RateLimiterChain;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
public class RateLimitController {
    private final RequestContextFactory requestContextFactory;
    private final RateLimiterChain rateLimiter;

    public RateLimitController(RequestContextFactory requestContextFactory, ApiKeyRateLimiter apiKeyRateLimiter) {
        this.requestContextFactory =
                requestContextFactory;
        this.rateLimiter =
                new RateLimiterChain(
                        List.of(apiKeyRateLimiter)
                );
    }

    @GetMapping("/check")
    public ResponseEntity<String> check(HttpServletRequest request){
        RequestContext context = requestContextFactory.fromHttpRequest(request);
        boolean allowed = rateLimiter.allow(context);
        return allowed ? ResponseEntity.ok("ALLOWED") :
                ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("RATE LIMIT EXCEEDED");
    }
}
