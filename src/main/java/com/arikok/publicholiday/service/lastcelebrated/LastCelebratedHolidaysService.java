package com.arikok.publicholiday.service.lastcelebrated;

import com.arikok.publicholiday.service.holidays.PublicHolidayDtoItem;
import com.arikok.publicholiday.service.holidays.PublicHolidaysQuery;
import com.arikok.publicholiday.service.holidays.PublicHolidaysService;
import com.arikok.publicholiday.util.DateUtil;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class LastCelebratedHolidaysService {

  private static final Logger log = LoggerFactory.getLogger(LastCelebratedHolidaysService.class);

  private final PublicHolidaysService publicHolidaysService;

  public LastCelebratedHolidaysService(
      PublicHolidaysService publicHolidaysService) {
    this.publicHolidaysService = publicHolidaysService;
  }

  public Mono<List<LastCelebratedHolidaysSummaryDto>> getLastCelebratedHolidays(
      FindLastCelebratedHolidaysQuery findLastCelebratedHolidaysQuery) {

    LocalDate today = LocalDate.now();
    int currentYear = DateUtil.getYear(today);
    int cutoffYear = currentYear - 10; // Stop the recursion if we cannot find in 10 years.

    return collectHolidaysRecursive(findLastCelebratedHolidaysQuery.countryCode(),
        findLastCelebratedHolidaysQuery.holidayCount(), currentYear, cutoffYear, today,
        new ArrayList<>())
        .map(holidays -> holidays.stream()
            .sorted(Comparator.comparing(PublicHolidayDtoItem::date).reversed())
            .limit(findLastCelebratedHolidaysQuery.holidayCount())
            .map(holiday -> new LastCelebratedHolidaysSummaryDto(holiday.date(), holiday.name()))
            .collect(Collectors.toList()));
  }

  private Mono<List<PublicHolidayDtoItem>> collectHolidaysRecursive(String countryCode,
      int holidayCount,
      int year,
      int cutoffYear,
      LocalDate today,
      List<PublicHolidayDtoItem> accumulator) {
    if (year < cutoffYear) {
      return Mono.just(accumulator);
    }

    return publicHolidaysService.getPublicHolidays(new PublicHolidaysQuery(year, countryCode))
        .flatMap(holidays -> {
          List<PublicHolidayDtoItem> filtered = holidays.stream()
              .filter(
                  holiday -> (year != DateUtil.getCurrentYear()) || holiday.date().isBefore(today))
              .toList();

          accumulator.addAll(filtered);

          if (accumulator.size() >= holidayCount) {
            return Mono.just(accumulator);
          } else {
            return collectHolidaysRecursive(countryCode, holidayCount, year - 1, cutoffYear, today,
                accumulator);
          }
        });
  }
}
