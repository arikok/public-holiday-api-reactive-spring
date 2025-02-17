package com.arikok.publicholiday.cache.redis;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RedisWarmer {

  private static final Logger log = LoggerFactory.getLogger(RedisWarmer.class);


  private final DynamicReactiveRedisTemplateRegistrar dynamicReactiveRedisTemplateRegistrar;

  public RedisWarmer(
      DynamicReactiveRedisTemplateRegistrar dynamicReactiveRedisTemplateRegistrar) {
    this.dynamicReactiveRedisTemplateRegistrar = dynamicReactiveRedisTemplateRegistrar;
  }

  @PostConstruct
  public void warmUpRedis() {

    dynamicReactiveRedisTemplateRegistrar.getTemplate(String.class)
        .opsForValue().set("warmup-key", "OK").block();
    Mono<String> warmUpMono = dynamicReactiveRedisTemplateRegistrar.getTemplate(String.class)
        .opsForValue().get("warmup-key");

    warmUpMono.subscribe(
        result -> log.info("Redis warm-up completed with result: {} ", result),
        error -> log.error("Redis warm-up failed: {}", error.getMessage())
    );

  }
}
