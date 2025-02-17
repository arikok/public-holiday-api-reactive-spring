package com.arikok.publicholiday.controller;

import com.arikok.publicholiday.service.lastcelebrated.FindLastCelebratedHolidaysQuery;
import com.arikok.publicholiday.service.lastcelebrated.LastCelebratedHolidaysService;
import com.arikok.publicholiday.service.lastcelebrated.LastCelebratedHolidaysSummaryDto;
import com.arikok.publicholiday.validation.ValidCountry;
import com.arikok.publicholiday.validation.ValidHolidayCount;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class LastCelebratedHolidaysController {

  private static final Logger log = LoggerFactory.getLogger(LastCelebratedHolidaysController.class);

  private final LastCelebratedHolidaysService lastCelebratedHolidaysService;

  public LastCelebratedHolidaysController(
      LastCelebratedHolidaysService lastCelebratedHolidaysService) {
    this.lastCelebratedHolidaysService = lastCelebratedHolidaysService;
  }

  @GetMapping("/v1/holiday/last/{country}")
  public Mono<List<LastCelebratedHolidaysSummaryDto>> getLastCelebratedHolidaysByCountry(
      @PathVariable("country") @ValidCountry String countryCode) {
    return getLastCelebratedHolidaysByCountryCore(3, countryCode);
  }

  @GetMapping("/v1/holiday/last/{country}/{count}")
  public Mono<List<LastCelebratedHolidaysSummaryDto>> getLastCelebratedHolidaysByCountryAndCount(
      @PathVariable("country") @ValidCountry String countryCode,
      @PathVariable("count") @ValidHolidayCount int count) {
    return getLastCelebratedHolidaysByCountryCore(count, countryCode);
  }

  private Mono<List<LastCelebratedHolidaysSummaryDto>> getLastCelebratedHolidaysByCountryCore(
      int count, String countryCode) {
    return lastCelebratedHolidaysService.getLastCelebratedHolidays(
        new FindLastCelebratedHolidaysQuery(count, countryCode));
  }

}
