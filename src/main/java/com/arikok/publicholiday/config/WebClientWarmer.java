package com.arikok.publicholiday.config;

import com.arikok.publicholiday.client.nagerapi.country.NagerAvailableCountriesClient;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebClientWarmer {

  private static final Logger log = LoggerFactory.getLogger(WebClientWarmer.class);

  // First request is slow on some environments (5 seconds)
  // Therefore we have to warm-up WebClient or change Netty Client to other HTTP Clients like Jetty
  //https://stackoverflow.com/questions/53943499/workaround-for-the-slowness-of-the-webclient-first-request

  private final NagerAvailableCountriesClient nagerAvailableCountriesClient;

  public WebClientWarmer(NagerAvailableCountriesClient nagerAvailableCountriesClient) {
    this.nagerAvailableCountriesClient = nagerAvailableCountriesClient;
  }

  @PostConstruct
  public void warmup() {
    log.info("WebClientWarmer warm-up started");
    nagerAvailableCountriesClient.fetchAvailableCountries()
        .subscribe(
            result -> log.info("WebClientWarmer warm-up completed"),
            error -> log.error("WebClientWarmer warm-up failed: {}", error.getMessage())
        );
  }
}
