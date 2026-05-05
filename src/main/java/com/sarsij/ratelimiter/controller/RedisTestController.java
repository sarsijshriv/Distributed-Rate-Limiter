package com.sarsij.ratelimiter.controller;

import com.sarsij.ratelimiter.service.RedisTestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisTestController {

    RedisTestService redisTestService;

    public RedisTestController(RedisTestService redisTestService){
        this.redisTestService = redisTestService;
    }

    @GetMapping("/redis-test")
    public String testRedis(){
        return redisTestService.testRedis();
    }
}
