package com.arikok.publicholiday.cache;

import reactor.core.publisher.Mono;

public interface CacheService {

  <T> void putLocal(String key, T value);

  <T> T getLocal(String key, Class<T> type);

  <T> void putRemote(String key, T value);

  <T> Mono<T> getRemote(String key, Class<T> type);

}
