package com.arikok.publicholiday.cache.caffeine;

import com.arikok.publicholiday.cache.CacheConstants;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class DynamicCaffeineCacheRegistrar {

  private static final Logger log = LoggerFactory.getLogger(DynamicCaffeineCacheRegistrar.class);
  private final GenericApplicationContext context;


  public DynamicCaffeineCacheRegistrar(GenericApplicationContext context) {
    this.context = context;
  }

  public <T> Cache<String, T> getStringKeyCache(Class<T> valueType) {
    String beanName = generateBeanName(valueType);
    if (context.containsBean(beanName)) {
      @SuppressWarnings("unchecked")
      Cache<String, T> existingCache = (Cache<String, T>) context.getBean(beanName, Cache.class);
      return existingCache;
    }
    Cache<String, T> cache = Caffeine.newBuilder()
        .expireAfterWrite(CacheConstants.COMMON_CACHE_DURATION)
        .maximumSize(CacheConstants.LOCAL_CACHE_SIZE)
        .build();
    context.registerBean(beanName, Cache.class, () -> cache);
    return cache;
  }

  private <T> String generateBeanName(Class<T> valueType) {
    return "caffeineCache_" + valueType.getSimpleName();
  }
}
