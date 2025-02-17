package com.arikok.publicholiday.client.nagerapi.publicholiday;

import java.time.LocalDate;
import java.util.List;

public record NagerPublicHolidayDtoItem(
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

}