package com.arikok.publicholiday.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientBuilderHelper {

  private static final Logger log = LoggerFactory.getLogger(WebClientBuilderHelper.class);

  private final WebClient.Builder webClientBuilder;

  public WebClientBuilderHelper(WebClient.Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  public WebClient buildWebClient(String baseUrl) {
    if (baseUrl == null || baseUrl.trim().isEmpty()) {
      throw new IllegalArgumentException("Base URL must not be null or empty");
    }
    return webClientBuilder.baseUrl(baseUrl).build();
  }

}
