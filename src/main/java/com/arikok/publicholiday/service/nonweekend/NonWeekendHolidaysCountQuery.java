package com.arikok.publicholiday.service.nonweekend;

import java.util.List;

public record NonWeekendHolidaysCountQuery(int year, List<String> countries) {

}
