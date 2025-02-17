package com.arikok.publicholiday.controller.model;

import com.arikok.publicholiday.validation.ValidCountries;
import com.arikok.publicholiday.validation.ValidYear;
import java.util.List;

public record NonWeekendHolidayCountRequest(
    @ValidYear
    Integer year,

    @ValidCountries
    List<String> countries) {

}
