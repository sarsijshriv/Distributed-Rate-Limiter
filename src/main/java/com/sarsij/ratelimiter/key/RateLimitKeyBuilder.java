package com.sarsij.ratelimiter.key;

import com.sarsij.ratelimiter.context.RequestContext;
import org.springframework.stereotype.Component;

@Component
public class RateLimitKeyBuilder {
    private static final String PREFIX = "rate_limit:";

    public String globalKey(){
        return PREFIX + "global";
    }

    public String apiKey(RequestContext context){
        return PREFIX + "apikey:" + context.getApiKey();
    }

    public String ipKey(RequestContext context){
        return  PREFIX + "ip:" + context.getClientIp();
    }

    public String endpointKey(RequestContext context){
        return PREFIX + "endpoint:"+ context.getEndpoint();
    }

}
