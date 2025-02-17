package com.arikok.publicholiday.service.nonweekend;

import com.arikok.publicholiday.service.holidays.PublicHolidaysQuery;
import com.arikok.publicholiday.service.holidays.PublicHolidaysService;
import com.arikok.publicholiday.util.DateUtil;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class NonWeekendHolidaysService {

  private static final Logger log = LoggerFactory.getLogger(NonWeekendHolidaysService.class);

  private final PublicHolidaysService publicHolidaysService;

  public NonWeekendHolidaysService(
      PublicHolidaysService publicHolidaysService) {
    this.publicHolidaysService = publicHolidaysService;
  }

  public Mono<List<NonWeekendHolidaysSummaryDto>> getNonWeekendHolidaysCounts(
      NonWeekendHolidaysCountQuery nonWeekendHolidaysCountQuery) {

    int year = nonWeekendHolidaysCountQuery.year();
    List<String> countries = nonWeekendHolidaysCountQuery.countries();

    return Flux.fromIterable(countries)
        .flatMap(countryCode ->
            publicHolidaysService.getPublicHolidays(new PublicHolidaysQuery(year, countryCode))
                .map(publicHolidays -> {

                  int count = (int) publicHolidays.stream()
                      .filter(holiday -> !DateUtil.isWeekend(holiday.date()))
                      .count();
                  return new NonWeekendHolidaysSummaryDto(countryCode, count);
                }))
        .sort((dto1, dto2) -> dto2.nonWeekendHolidayCount() -
            dto1.nonWeekendHolidayCount())
        .collectList();

  }

}
