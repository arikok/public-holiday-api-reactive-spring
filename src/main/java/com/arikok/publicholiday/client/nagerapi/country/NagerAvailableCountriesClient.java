package com.arikok.publicholiday.client.nagerapi.country;

import com.arikok.publicholiday.client.nagerapi.NagerApiException;
import com.arikok.publicholiday.client.nagerapi.NagerApiProperties;
import com.arikok.publicholiday.config.WebClientBuilderHelper;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class NagerAvailableCountriesClient {

  private final WebClient webClient;

  public NagerAvailableCountriesClient(
      WebClientBuilderHelper webClientBuilderHelper, NagerApiProperties nagerApiProperties) {
    this.webClient = webClientBuilderHelper.buildWebClient(nagerApiProperties.getBaseUrl());
  }

  public Mono<NagerAvailableCountryDto> fetchAvailableCountries() {
    return webClient.get()
        .uri("/api/v3/AvailableCountries")
        .retrieve()
        .onStatus(HttpStatusCode::is4xxClientError,
            clientResponse ->
                Mono.error(new NagerApiException(NagerApiException.CLIENT_ERROR)))
        .onStatus(HttpStatusCode::is5xxServerError,
            clientResponse ->
                Mono.error(new NagerApiException(NagerApiException.SERVER_ERROR)))
        .bodyToMono(new ParameterizedTypeReference<List<NagerAvailableCountryDtoItem>>() {
        })
        .map(countryDtos ->
            new NagerAvailableCountryDto(
                countryDtos.stream()
                    .collect(Collectors.toUnmodifiableMap(
                        NagerAvailableCountryDtoItem::countryCode,
                        dto -> dto)))
        );
  }
}

