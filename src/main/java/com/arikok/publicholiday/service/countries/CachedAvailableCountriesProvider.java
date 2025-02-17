package com.arikok.publicholiday.service.countries;

import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class CachedAvailableCountriesProvider {

  // this needed for calls from the validations - TODO

  private final AvailableCountriesService availableCountriesService;
  private ConcurrentHashMap<String, AvailableCountryDtoItem> cachedAvailableCountries;

  public CachedAvailableCountriesProvider(AvailableCountriesService availableCountriesService) {
    this.availableCountriesService = availableCountriesService;
  }

  @PostConstruct
  public void init() {
    cachedAvailableCountries = new ConcurrentHashMap<>(
        availableCountriesService.getAvailableCountries().block());
  }

  public Map<String, AvailableCountryDtoItem> getCachedAvailableCountries() {
    return cachedAvailableCountries;
  }

  public boolean isValidCountry(String country) {
    return cachedAvailableCountries != null && cachedAvailableCountries.containsKey(country);
  }
}
