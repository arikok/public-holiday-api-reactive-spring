package com.arikok.publicholiday.service.countries;

import java.util.Map;

public record AvailableCountryDto(
    Map<String, AvailableCountryDtoItem> countries
) {

}