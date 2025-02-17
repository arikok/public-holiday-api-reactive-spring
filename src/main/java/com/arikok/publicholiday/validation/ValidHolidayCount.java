package com.arikok.publicholiday.validation;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = {})
@Target({FIELD, METHOD, PARAMETER, ANNOTATION_TYPE})
@Retention(RUNTIME)
@NotNull(message = "Count must not be null")
@Positive(message = "Count must be a positive number")
@Min(value = 1, message = "Count must be at least 1")
@Max(value = 50, message = "Count must be max 50")
public @interface ValidHolidayCount {

  String message() default "Invalid Count";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
