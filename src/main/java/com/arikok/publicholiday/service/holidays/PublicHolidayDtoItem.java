package com.arikok.publicholiday.service.holidays;

import com.arikok.publicholiday.client.nagerapi.publicholiday.NagerPublicHolidayDtoItem;
import java.time.LocalDate;
import java.util.List;

public record PublicHolidayDtoItem(
    LocalDate date,
    String localName,
    String name,
    String countryCode,
    boolean fixed,
    boolean global,
    List<String> counties,
    Integer launchYear,
    List<String> types
) {

  static PublicHolidayDtoItem from(NagerPublicHolidayDtoItem nagerItem) {
    return new PublicHolidayDtoItem(nagerItem.date(), nagerItem.localName(), nagerItem.name(),
        nagerItem.countryCode(), nagerItem.fixed(), nagerItem.global(), nagerItem.counties(),
        nagerItem.launchYear(), nagerItem.types());
  }
}