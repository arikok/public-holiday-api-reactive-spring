package com.arikok.publicholiday.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ValidHolidayCountTest {

  private static Validator validator;

  @BeforeAll
  static void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validCountShouldPassValidation() {
    HolidayCountTestBean bean = new HolidayCountTestBean(10);
    Set<ConstraintViolation<HolidayCountTestBean>> violations = validator.validate(bean);
    assertThat(violations).isEmpty();
  }

  @Test
  void nullCountShouldFailNotNullValidation() {
    HolidayCountTestBean bean = new HolidayCountTestBean(null);
    Set<ConstraintViolation<HolidayCountTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Count must not be null");
  }

  @Test
  void negativeCountShouldFailPositiveValidation() {
    HolidayCountTestBean bean = new HolidayCountTestBean(-5);
    Set<ConstraintViolation<HolidayCountTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Count must be a positive number");
  }

  @Test
  void countZeroShouldFailPositiveAndMinValidation() {
    // Zero is not positive and is below the minimum of 1.
    HolidayCountTestBean bean = new HolidayCountTestBean(0);
    Set<ConstraintViolation<HolidayCountTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Count must be a positive number", "Count must be at least 1");
  }

  @Test
  void countAboveMaxShouldFailMaxValidation() {
    HolidayCountTestBean bean = new HolidayCountTestBean(100);
    Set<ConstraintViolation<HolidayCountTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Count must be max 50");
  }
}
