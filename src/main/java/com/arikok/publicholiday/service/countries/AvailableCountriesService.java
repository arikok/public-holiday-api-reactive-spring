package com.arikok.publicholiday.service.countries;

import com.arikok.publicholiday.cache.CacheService;
import com.arikok.publicholiday.client.nagerapi.country.NagerAvailableCountriesClient;
import com.arikok.publicholiday.client.nagerapi.country.NagerAvailableCountryDtoItem;
import com.arikok.publicholiday.service.BaseCacheableService;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class AvailableCountriesService extends BaseCacheableService {

  private final static String AVAILABLE_COUNTRIES_CACHE_KEY = "AVAILABLE_COUNTRIES_CACHE_KEY";
  private final NagerAvailableCountriesClient nagerAvailableCountriesClient;

  public AvailableCountriesService(
      NagerAvailableCountriesClient nagerAvailableCountriesClient, CacheService cacheService) {
    super(cacheService);
    this.nagerAvailableCountriesClient = nagerAvailableCountriesClient;
  }

  public Mono<Map<String, AvailableCountryDtoItem>> getAvailableCountries() {

    Supplier<Mono<AvailableCountryDto>> supplier =
        () -> nagerAvailableCountriesClient.fetchAvailableCountries()
            .map(nagerItems -> new AvailableCountryDto(
                nagerItems.countries().values().stream().collect(
                    Collectors.toMap(NagerAvailableCountryDtoItem::countryCode,
                        AvailableCountryDtoItem::from))));

    return getCachedValue(
        AVAILABLE_COUNTRIES_CACHE_KEY,
        AvailableCountryDto.class,
        supplier
    ).map(AvailableCountryDto::countries);

  }
}
