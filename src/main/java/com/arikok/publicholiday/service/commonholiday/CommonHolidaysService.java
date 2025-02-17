package com.arikok.publicholiday.service.commonholiday;

import com.arikok.publicholiday.service.holidays.PublicHolidayDtoItem;
import com.arikok.publicholiday.service.holidays.PublicHolidaysQuery;
import com.arikok.publicholiday.service.holidays.PublicHolidaysService;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import reactor.util.function.Tuples;

@Service
public class CommonHolidaysService {

  private final PublicHolidaysService publicHolidaysService;

  public CommonHolidaysService(PublicHolidaysService publicHolidaysService) {
    this.publicHolidaysService = publicHolidaysService;
  }
  
  public Mono<List<Map<String, Map<String, String>>>> getCommonHolidaysMapping(
      CommonHolidayQuery commonHolidayQuery) {
    // For each country code, retrieve the public holidays and map them to a tuple:
    // (countryCode, Map<LocalDate, String>), where the inner map is date -> localName.
    Mono<List<Tuple2<String, Map<LocalDate, String>>>> listOfCountryMaps = Flux.fromIterable(
            commonHolidayQuery.countries())
        .flatMap(countryCode ->
            publicHolidaysService.getPublicHolidays(
                    new PublicHolidaysQuery(commonHolidayQuery.year(), countryCode))
                .map(holidays -> {
                  Map<LocalDate, String> dateToLocalName = holidays.stream()
                      .collect(Collectors.toMap(
                          PublicHolidayDtoItem::date,
                          PublicHolidayDtoItem::localName
                      ));
                  return Tuples.of(countryCode, dateToLocalName);
                })
        )
        .collectList();

    return listOfCountryMaps.map(listOfTuples -> {
      // Compute the intersection of dates across all countries.
      Set<LocalDate> commonDates = new HashSet<>();
      if (!listOfTuples.isEmpty()) {
        commonDates.addAll(listOfTuples.get(0).getT2().keySet());
        for (int i = 1; i < listOfTuples.size(); i++) {
          commonDates.retainAll(listOfTuples.get(i).getT2().keySet());
        }
      }

      // For each common date, build a mapping of country code to local name.
      List<Map<String, Map<String, String>>> result = commonDates.stream()
          .map(date -> {
            Map<String, String> innerMap = new HashMap<>();
            for (Tuple2<String, Map<LocalDate, String>> tuple : listOfTuples) {
              String countryCode = tuple.getT1();
              String localName = tuple.getT2().get(date);
              innerMap.put(countryCode, localName);
            }
            // Use the date (formatted as a string) as the key.
            Map<String, Map<String, String>> outerMap = new HashMap<>();
            outerMap.put(date.toString(), innerMap);
            return outerMap;
          })
          .sorted(Comparator.comparing(map -> map.keySet().iterator().next()))
          .collect(Collectors.toList());

      return result;
    });
  }
}
