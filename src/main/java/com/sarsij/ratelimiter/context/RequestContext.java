package com.sarsij.ratelimiter.context;

import lombok.Getter;

@Getter
public class RequestContext {
    private final String apiKey;
    private final String clientIp;
    private final String endpoint;
    private final Long timestamp;

    public RequestContext(
            String apiKey,
            String clientIp,
            String endpoint,
            long timestamp) {
        this.apiKey = apiKey;
        this.clientIp = clientIp;
        this.endpoint = endpoint;
        this.timestamp = timestamp;
    }

}
