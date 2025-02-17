package com.arikok.publicholiday.service.holidays;

import com.arikok.publicholiday.cache.CacheService;
import com.arikok.publicholiday.client.nagerapi.publicholiday.NagerPublicHolidayClient;
import com.arikok.publicholiday.service.BaseCacheableService;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class PublicHolidaysService extends BaseCacheableService {

  private static final Logger log = LoggerFactory.getLogger(PublicHolidaysService.class);

  private final static String PUBLIC_HOLIDAYS_CACHE_KEY = "PUBLIC_HOLIDAYS_CACHE_KEY_";

  private final NagerPublicHolidayClient nagerPublicHolidayClient;

  public PublicHolidaysService(
      CacheService cacheService,
      NagerPublicHolidayClient nagerPublicHolidayClient) {
    super(cacheService);
    this.nagerPublicHolidayClient = nagerPublicHolidayClient;

  }

  public Mono<List<PublicHolidayDtoItem>> getPublicHolidays(
      PublicHolidaysQuery publicHolidaysQuery) {

    Supplier<Mono<PublicHolidayDto>> supplier =
        () -> {
          log.info("nagerPublicHolidayClient.fetchPublicHolidays-cache-miss: {} {}",
              publicHolidaysQuery.countryCode(), publicHolidaysQuery.year());
          return nagerPublicHolidayClient.fetchPublicHolidays(publicHolidaysQuery.to())
              .map(nagerItems -> new PublicHolidayDto(
                  nagerItems.stream().map(PublicHolidayDtoItem::from)
                      .collect(Collectors.toList())));
        };

    return getCachedValue(
        toHashKey(publicHolidaysQuery),
        PublicHolidayDto.class,
        supplier
    ).map(PublicHolidayDto::holidays);

  }

  private String toHashKey(PublicHolidaysQuery publicHolidaysQuery) {
    return PUBLIC_HOLIDAYS_CACHE_KEY + publicHolidaysQuery.countryCode()
        + publicHolidaysQuery.year();
  }
}
