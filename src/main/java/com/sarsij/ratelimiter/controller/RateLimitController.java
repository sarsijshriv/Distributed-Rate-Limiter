package com.sarsij.ratelimiter.controller;

import com.sarsij.ratelimiter.context.RequestContext;
import com.sarsij.ratelimiter.context.RequestContextFactory;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimitController {
    RequestContextFactory requestContextFactory;
    public RateLimitController(RequestContextFactory requestContextFactory){
        this.requestContextFactory = requestContextFactory;
    }

    @GetMapping("/check")
    public String check(HttpServletRequest request){
        RequestContext context = requestContextFactory.fromHttpRequest(request);
        return "OK - " + context.getApiKey()
                + " | IP: " + context.getClientIp()
                + " | Endpoint: " + context.getEndpoint();
    }
}
