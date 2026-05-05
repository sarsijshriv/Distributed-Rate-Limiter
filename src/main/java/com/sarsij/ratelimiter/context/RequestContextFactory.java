package com.sarsij.ratelimiter.context;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

@Component
public class RequestContextFactory {

    public RequestContext fromHttpRequest(HttpServletRequest request) {

        String apiKey = extractApiKey(request);
        String clientIp = extractClientIp(request);
        String endpoint = normalizeEndpoint(request);
        long timestamp = System.currentTimeMillis();

        return new RequestContext(
                apiKey,
                clientIp,
                endpoint,
                timestamp
        );
    }

    private String extractApiKey(HttpServletRequest request) {

        String apiKey = request.getHeader("X-API-Key");

        if (apiKey == null || apiKey.isBlank()) {
            throw new RuntimeException("Missing X-API-Key header");
        }

        return apiKey.trim();
    }

    private String extractClientIp(HttpServletRequest request) {

        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }

    private String normalizeEndpoint(HttpServletRequest request) {

        String uri = request.getRequestURI();

        return uri;
    }
}