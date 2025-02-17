package com.arikok.publicholiday.util;

import java.time.DayOfWeek;
import java.time.LocalDate;

public final class DateUtil {

  public static int getYear(LocalDate date) {
    return date.getYear();
  }

  public static int getCurrentYear() {
    return LocalDate.now().getYear();
  }

  public static boolean isWeekend(LocalDate date) {
    DayOfWeek dayOfWeek = date.getDayOfWeek();
    return dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY;
  }


}
