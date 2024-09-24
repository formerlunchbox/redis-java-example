package com.formerlunchbox.redis.redission;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestRedission {
  @Autowired
  private RedissonClient redissonClient;

  @Test
  void testRedission() {
    redissonClient.getBucket("testKey").set("testValue");
    String temp = redissonClient.getBucket("testKey").get().toString();
    System.out.println(temp);
    Assertions.assertEquals("testValue", temp);
  }

  @Test
  void testRedissionLock() {
    RLock lock = redissonClient.getLock("lock");
    try {
      lock.lock();
      lock.lock();
    } finally {
      try {
        lock.unlock();
      } catch (Exception e) {
        System.out.println(e.getClass().toString());
        System.out.println("Unlock error: " + e.getMessage());
      }
      lock.unlock();
      Assertions.assertFalse(lock.isLocked());
    }
  }
}
