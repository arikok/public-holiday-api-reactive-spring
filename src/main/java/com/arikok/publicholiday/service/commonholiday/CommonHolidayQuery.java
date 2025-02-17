package com.arikok.publicholiday.service.commonholiday;

import java.util.List;

public record CommonHolidayQuery(
    int year,
    List<String> countries) {

}
