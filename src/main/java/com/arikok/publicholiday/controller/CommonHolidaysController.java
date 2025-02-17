package com.arikok.publicholiday.controller;

import com.arikok.publicholiday.service.commonholiday.CommonHolidayQuery;
import com.arikok.publicholiday.service.commonholiday.CommonHolidaysService;
import com.arikok.publicholiday.validation.ValidCountry;
import com.arikok.publicholiday.validation.ValidYear;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@Validated
public class CommonHolidaysController {

  private static final Logger log = LoggerFactory.getLogger(CommonHolidaysController.class);

  private final CommonHolidaysService commonHolidaysService;

  public CommonHolidaysController(CommonHolidaysService commonHolidaysService) {
    this.commonHolidaysService = commonHolidaysService;
  }

  /**
   * [ { "2025-01-01" : { "US": "New Year's Day", "CA": "Jour de l'an" } }, { "2025-07-04" : { "US":
   * "Independence Day", "CA": "Fête de l'indépendance" } } ]
   */

  @GetMapping("/v1/holiday/common/{year}/{country1}/{country2}")
  public Mono<List<Map<String, Map<String, String>>>> getCommonHolidaysByPath(
      @PathVariable("year") @ValidYear int year,
      @PathVariable("country1") @ValidCountry String countryCode1,
      @PathVariable("country2") @ValidCountry String countryCode2) {
    return commonHolidaysService.getCommonHolidaysMapping(
        new CommonHolidayQuery(year, Arrays.asList(countryCode1, countryCode2)));
  }

}
