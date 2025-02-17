package com.arikok.publicholiday.cache;

import com.arikok.publicholiday.cache.caffeine.DynamicCaffeineCacheRegistrar;
import com.arikok.publicholiday.cache.redis.DynamicReactiveRedisTemplateRegistrar;
import com.github.benmanes.caffeine.cache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CaffeineRedisCache implements CacheService {

  private static final Logger log = LoggerFactory.getLogger(CaffeineRedisCache.class);

  private final DynamicReactiveRedisTemplateRegistrar dynamicReactiveRedisTemplateRegistrar;
  private final DynamicCaffeineCacheRegistrar dynamicCaffeineCacheRegistrar;

  public CaffeineRedisCache(
      DynamicReactiveRedisTemplateRegistrar dynamicReactiveRedisTemplateRegistrar,
      DynamicCaffeineCacheRegistrar dynamicCaffeineCacheRegistrar) {
    this.dynamicReactiveRedisTemplateRegistrar = dynamicReactiveRedisTemplateRegistrar;
    this.dynamicCaffeineCacheRegistrar = dynamicCaffeineCacheRegistrar;
  }

  @Override
  public <T> void putLocal(String key, T value) {
    @SuppressWarnings("unchecked")
    Cache<String, T> cache = dynamicCaffeineCacheRegistrar.getStringKeyCache(
        (Class<T>) value.getClass());
    cache.put(key, value);
  }

  @Override
  public <T> T getLocal(String key, Class<T> type) {
    return dynamicCaffeineCacheRegistrar.getStringKeyCache(
        type).getIfPresent(key);
  }

  @Override
  public <T> void putRemote(String key, T value) {
    @SuppressWarnings("unchecked")
    ReactiveRedisTemplate<String, T> reactiveRedisTemplate = dynamicReactiveRedisTemplateRegistrar.getTemplate(
        (Class<T>) value.getClass());
    reactiveRedisTemplate.opsForValue().set(key, value, CacheConstants.COMMON_CACHE_DURATION);

  }

  @Override
  public <T> Mono<T> getRemote(String key, Class<T> type) {
    return dynamicReactiveRedisTemplateRegistrar.getTemplate(type).opsForValue()
        .get(key);
  }

}
