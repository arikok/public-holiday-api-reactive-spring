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
@NotNull(message = "Year must not be null")
@Positive(message = "Year must be a positive number")
@Min(value = 1950, message = "Year must be at least 1950")
@Max(value = 2100, message = "Year must be at max 2100")
public @interface ValidYear {

  String message() default "Invalid year";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
