package com.arikok.publicholiday.service.commonholiday;

import com.arikok.publicholiday.client.nagerapi.country.NagerAvailableCountryDtoItem;

public record CommonHolidayDtoItem(
    String countryCode,
    String name
) {

  static CommonHolidayDtoItem from(NagerAvailableCountryDtoItem nagerAvailableCountryDtoItem) {
    return new CommonHolidayDtoItem(nagerAvailableCountryDtoItem.countryCode(),
        nagerAvailableCountryDtoItem.name());
  }
}