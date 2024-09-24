package com.formerlunchbox.redis;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class TestLinkRedis {
  @Autowired
  private RedisTemplate redisTemplate;

  @SuppressWarnings("unchecked")
  @Test
  void testLinkRedis() {
    String value = "value";
    redisTemplate.opsForValue().set("key", value);
    redisTemplate.opsForValue().getAndDelete("key");
    redisTemplate.opsForValue().set("key2", value);
    String temp = redisTemplate.opsForValue().get("key2").toString();
    System.out.println(temp);
    Assertions.assertEquals(value, temp);
  }
}
