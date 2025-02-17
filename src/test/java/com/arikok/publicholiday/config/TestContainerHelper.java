package com.arikok.publicholiday.config;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class TestContainerHelper {

  public static void initializeRedisContainer() {
    GenericContainer<?> redis =
        new GenericContainer<>(DockerImageName.parse("redis:7.4.2")).withExposedPorts(6379);
    redis.start();
    System.setProperty("spring.redis.host", redis.getHost());
    System.setProperty("spring.redis.port", redis.getMappedPort(6379).toString());
  }

}
