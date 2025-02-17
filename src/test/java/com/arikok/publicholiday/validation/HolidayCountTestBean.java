package com.arikok.publicholiday.validation;

public class HolidayCountTestBean {

  @ValidHolidayCount
  private Integer count;

  public HolidayCountTestBean(Integer count) {
    this.count = count;
  }

  public Integer getCount() {
    return count;
  }

  public void setCount(Integer count) {
    this.count = count;
  }
}
