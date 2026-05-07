package com.sarsij.ratelimiter.store;

public interface TokenBucketStore {

    boolean tryConsume(String key, int capacity, int refillTokens, long refillDurationMillis);
}
