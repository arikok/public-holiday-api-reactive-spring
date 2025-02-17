package com.arikok.publicholiday.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CountryValidatorTest {

  private CachedAvailableCountriesProvider cachedAvailableCountriesProvider;
  private CountryValidator validator;
  private ConstraintValidatorContext context;

  @BeforeEach
  void setUp() {
    cachedAvailableCountriesProvider = mock(CachedAvailableCountriesProvider.class);
    validator = new CountryValidator(cachedAvailableCountriesProvider);
    context = mock(ConstraintValidatorContext.class);
  }

  @Test
  void givenNullCountry_whenIsValid_thenReturnsFalse() {
    boolean valid = validator.isValid(null, context);
    assertThat(valid).isFalse();
  }

  @Test
  void givenNullAvailableCountries_whenIsValid_thenReturnsFalse() {
    when(cachedAvailableCountriesProvider.getCachedAvailableCountries()).thenReturn(null);
    boolean valid = validator.isValid("US", context);
    assertThat(valid).isFalse();
  }

  @Test
  void givenCountryNotInAvailableCountries_whenIsValid_thenReturnsFalse() {
    when(cachedAvailableCountriesProvider.getCachedAvailableCountries())
        .thenReturn(Collections.emptyMap());
    boolean valid = validator.isValid("US", context);
    assertThat(valid).isFalse();
  }

  @Test
  void givenCountryInAvailableCountries_whenIsValid_thenReturnsTrue() {
    //todo
  }
}
