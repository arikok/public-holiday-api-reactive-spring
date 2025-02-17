package com.arikok.publicholiday.client.nagerapi.publicholiday;

import com.arikok.publicholiday.cache.redis.RedisWarmer;
import com.arikok.publicholiday.client.nagerapi.NagerApiException;
import com.arikok.publicholiday.client.nagerapi.NagerApiProperties;
import com.arikok.publicholiday.config.WebClientBuilderHelper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NagerPublicHolidayClient {

  private static final Logger log = LoggerFactory.getLogger(RedisWarmer.class);
  private final WebClient webClient;

  public NagerPublicHolidayClient(
      WebClientBuilderHelper webClientBuilderHelper, NagerApiProperties nagerApiProperties) {
    this.webClient = webClientBuilderHelper.buildWebClient(nagerApiProperties.getBaseUrl());
  }

  public Mono<List<NagerPublicHolidayDtoItem>> fetchPublicHolidays(
      NagerPublicHolidayQuery nagerPublicHolidayQuery) {

    return webClient.get()
        .uri("/api/v3/PublicHolidays/{year}/{countryCode}",
            nagerPublicHolidayQuery.year(),
            nagerPublicHolidayQuery.countryCode())
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new NagerApiException(NagerApiException.CLIENT_ERROR)))
        .onStatus(HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new NagerApiException(NagerApiException.SERVER_ERROR)))
        .onStatus(HttpStatusCode::is5xxServerError,
            clientResponse -> Mono.error(new NagerApiException("Server error!")))
        .bodyToMono(new ParameterizedTypeReference<List<NagerPublicHolidayDtoItem>>() {
        });

  }
}
