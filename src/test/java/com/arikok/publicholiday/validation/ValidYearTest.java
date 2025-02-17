package com.arikok.publicholiday.validation;

import static org.assertj.core.api.Assertions.assertThat;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class ValidYearTest {

  private static Validator validator;

  @BeforeAll
  static void setup() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  void validYearShouldPassValidation() {
    YearTestBean bean = new YearTestBean(2000);
    Set<ConstraintViolation<YearTestBean>> violations = validator.validate(bean);
    assertThat(violations).isEmpty();
  }

  @Test
  void nullYearShouldFailNotNullValidation() {
    YearTestBean bean = new YearTestBean(null);
    Set<ConstraintViolation<YearTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Year must not be null");
  }

  @Test
  void negativeYearShouldFailPositiveValidation() {
    YearTestBean bean = new YearTestBean(-5);
    Set<ConstraintViolation<YearTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Year must be a positive number");
  }

  @Test
  void yearBelowMinimumShouldFailMinValidation() {
    // Using 1940 which is below the @Min(value = 1950)
    YearTestBean bean = new YearTestBean(1940);
    Set<ConstraintViolation<YearTestBean>> violations = validator.validate(bean);
    // Note: The violation message is "Year must be at least 1975" per the annotation,
    // even though the value is set to 1950. Adjust if you change the annotation.
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Year must be at least 1950");
  }

  @Test
  void yearAboveMaximumShouldFailMaxValidation() {
    YearTestBean bean = new YearTestBean(2200);
    Set<ConstraintViolation<YearTestBean>> violations = validator.validate(bean);
    assertThat(violations)
        .extracting(ConstraintViolation::getMessage)
        .contains("Year must be at max 2100");
  }
}
