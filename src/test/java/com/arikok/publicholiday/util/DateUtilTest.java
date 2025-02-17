package com.arikok.publicholiday.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;

class DateUtilTest {

  @Test
  void testGetYear() {
    LocalDate date = LocalDate.of(2021, 5, 15);
    int year = DateUtil.getYear(date);
    assertEquals(2021, year, "getYear should return the correct year for a given date");
  }

  @Test
  void testGetCurrentYear() {
    int currentYear = LocalDate.now().getYear();
    assertEquals(currentYear, DateUtil.getCurrentYear(),
        "getCurrentYear should match LocalDate.now().getYear()");
  }

  @Test
  void testIsWeekendForSaturday() {
    // May 15, 2021 is a Saturday
    LocalDate saturday = LocalDate.of(2021, 5, 15);
    assertTrue(DateUtil.isWeekend(saturday), "isWeekend should return true for Saturday");
  }

  @Test
  void testIsWeekendForSunday() {
    // May 16, 2021 is a Sunday
    LocalDate sunday = LocalDate.of(2021, 5, 16);
    assertTrue(DateUtil.isWeekend(sunday), "isWeekend should return true for Sunday");
  }

  @Test
  void testIsWeekendForWeekday() {
    // May 17, 2021 is a Monday
    LocalDate monday = LocalDate.of(2021, 5, 17);
    assertFalse(DateUtil.isWeekend(monday), "isWeekend should return false for a weekday");
  }

}
