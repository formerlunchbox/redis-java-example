package com.formerlunchbox.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.ReadMode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

  @Value("${spring.redis.sentinel.master}")
  private String masterName;
  @Value("${spring.redis.sentinel.password}")
  private String sentinelPassword;
  @Value("${spring.redis.sentinel.username}")
  private String sentinelUsername;
  @Value("${spring.redis.sentinel.nodes}")
  private String sentinelNodes;
  @Value("${spring.redis.username}")
  private String username;
  @Value("${spring.redis.password}")
  private String password;
  @Value("${spring.redis.database}")
  private int database;

  @Bean
  public RedissonClient redissonClient() {
    if (sentinelNodes == null || sentinelNodes.isEmpty()) {
      throw new IllegalArgumentException("sentinelNodes is empty");
    }
    String[] nodes = sentinelNodes.split(",");
    for (int i = 0; i < nodes.length; i++) {
      nodes[i] = "redis://" + nodes[i];
    }

    Config config = new Config();
    config.useSentinelServers()
        .setMasterName(masterName)
        .addSentinelAddress(nodes)
        .setSentinelPassword(sentinelPassword)
        .setSentinelUsername(sentinelUsername)
        .setDatabase(database)
        .setUsername(username)
        .setPassword(password)
        .setCheckSentinelsList(false)
        .setReadMode(ReadMode.SLAVE);

    return Redisson.create(config);
  }
}