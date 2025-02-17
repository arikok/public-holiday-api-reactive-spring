package com.arikok.publicholiday.validation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Constraint(validatedBy = CountriesValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
@NotNull(message = "Countries list must not be null")
public @interface ValidCountries {

  String message() default "One or more provided country codes are invalid.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
