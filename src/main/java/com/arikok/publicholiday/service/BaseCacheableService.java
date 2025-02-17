package com.arikok.publicholiday.service;

import com.arikok.publicholiday.cache.CacheService;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;

public abstract class BaseCacheableService {

  private static final Logger log = LoggerFactory.getLogger(BaseCacheableService.class);

  protected final CacheService cacheService;

  public BaseCacheableService(CacheService cacheService) {
    this.cacheService = cacheService;
  }

  protected <T> Mono<T> getCachedValue(String key, Class<T> type, Supplier<Mono<T>> supplier) {
    T localValue = cacheService.getLocal(key, type);
    if (localValue != null) {
      return Mono.just(localValue);
    }

    return cacheService.getRemote(key, type)
        .switchIfEmpty(
            supplier.get().doOnNext(value -> {
              cacheService.putLocal(key, value);
              cacheService.putRemote(key, value);
            })
        );
  }

}