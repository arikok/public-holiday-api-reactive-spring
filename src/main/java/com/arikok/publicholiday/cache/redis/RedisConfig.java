package com.arikok.publicholiday.cache.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

@Configuration
public class RedisConfig {

  @Value("${spring.redis.host:redis}")
  private String redisHost;

  @Value("${spring.redis.port:6379}")
  private int redisPort;

  @Primary
  @Bean
  public ReactiveRedisConnectionFactory reactiveRedisConnectionFactory() {
    LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost,
        redisPort);
    lettuceConnectionFactory.setValidateConnection(true);
    return lettuceConnectionFactory;
  }

}
