package com.arikok.publicholiday.service.countries;

import com.arikok.publicholiday.client.nagerapi.country.NagerAvailableCountryDtoItem;

public record AvailableCountryDtoItem(
    String countryCode,
    String name
) {

  static AvailableCountryDtoItem from(NagerAvailableCountryDtoItem nagerAvailableCountryDtoItem) {
    return new AvailableCountryDtoItem(nagerAvailableCountryDtoItem.countryCode(),
        nagerAvailableCountryDtoItem.name());
  }
}