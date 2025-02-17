package com.arikok.publicholiday.controller.model;

import com.arikok.publicholiday.validation.ValidCountries;
import com.arikok.publicholiday.validation.ValidYear;
import java.util.List;

public record CommonHolidayRequest(
    @ValidYear
    Integer year,

    @ValidCountries
    List<String> countries) {

}
