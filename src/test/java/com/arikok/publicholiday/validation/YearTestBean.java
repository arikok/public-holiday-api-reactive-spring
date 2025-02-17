package com.arikok.publicholiday.validation;

public class YearTestBean {

  @ValidYear
  private Integer year;

  public YearTestBean(Integer year) {
    this.year = year;
  }

  public Integer getYear() {
    return year;
  }

  public void setYear(Integer year) {
    this.year = year;
  }
}
