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
    String temp = redisTemplate.opsForValue().get("key").toString();
    System.out.println(temp);
    Assertions.assertEquals(value, temp);
  }
}
