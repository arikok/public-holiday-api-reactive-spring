package com.arikok.publicholiday.client.nagerapi.country;

import java.util.Map;

public record NagerAvailableCountryDto(
    Map<String, NagerAvailableCountryDtoItem> countries
) {

}