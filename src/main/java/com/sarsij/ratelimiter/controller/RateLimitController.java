package com.sarsij.ratelimiter.controller;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.context.RequestContextFactory;
import com.sarsij.ratelimiter.key.RateLimitKeyBuilder;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
    RequestContextFactory requestContextFactory;
    RateLimitKeyBuilder keyBuilder;
    public RateLimitController(RequestContextFactory requestContextFactory, RateLimitKeyBuilder keyBuilder){
        this.requestContextFactory = requestContextFactory;
        this.keyBuilder = keyBuilder;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        RequestContext context = requestContextFactory.fromHttpRequest(request);
        return keyBuilder.apiKey(context);
    }
}
