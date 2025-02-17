package com.arikok.publicholiday.controller;

import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysCountQuery;
import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysService;
import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysSummaryDto;
import com.arikok.publicholiday.validation.ValidCountry;
import com.arikok.publicholiday.validation.ValidYear;
import java.util.Arrays;
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
public class NonWeekendHolidaysController {

  private static final Logger log = LoggerFactory.getLogger(NonWeekendHolidaysController.class);

  private final NonWeekendHolidaysService nonWeekendHolidaysService;

  public NonWeekendHolidaysController(
      NonWeekendHolidaysService nonWeekendHolidaysService) {
    this.nonWeekendHolidaysService = nonWeekendHolidaysService;
  }

  @GetMapping("/v1/holiday/non-weekend/{year}/{country1}/{country2}")
  public Mono<List<NonWeekendHolidaysSummaryDto>> getNonWeekendHolidaysByPath(
      @PathVariable("year") @ValidYear int year,
      @PathVariable("country1") @ValidCountry String countryCode1,
      @PathVariable("country2") @ValidCountry String countryCode2) {
    return nonWeekendHolidaysService.getNonWeekendHolidaysCounts(
        new NonWeekendHolidaysCountQuery(year, Arrays.asList(countryCode1, countryCode2)));
  }
  
}
