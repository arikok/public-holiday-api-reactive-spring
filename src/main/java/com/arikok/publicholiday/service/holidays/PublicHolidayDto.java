package com.arikok.publicholiday.service.holidays;

import java.util.List;

public record PublicHolidayDto(
    List<PublicHolidayDtoItem> holidays
) {

}