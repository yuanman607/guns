package com.stylefeng.guns.rest.common.util;

import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class MyStartupRunner implements CommandLineRunner {
    @Resource
    private StringRedisTemplate redis;
    @Override
    public void run(String... args) throws Exception {
        System.out.println(redis.opsForValue().get("route")+"----2------");
    }
}
