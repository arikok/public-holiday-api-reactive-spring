package com.arikok.publicholiday.service.holidays;

import com.arikok.publicholiday.client.nagerapi.publicholiday.NagerPublicHolidayQuery;

public record PublicHolidaysQuery(
    int year,
    String countryCode
) {

  NagerPublicHolidayQuery to() {
    return new NagerPublicHolidayQuery(year, countryCode);
  }

}