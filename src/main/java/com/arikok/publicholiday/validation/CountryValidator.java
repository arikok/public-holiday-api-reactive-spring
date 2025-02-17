package com.arikok.publicholiday.validation;

import com.arikok.publicholiday.service.countries.AvailableCountryDtoItem;
import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CountryValidator implements ConstraintValidator<ValidCountry, String> {

  private final CachedAvailableCountriesProvider cachedAvailableCountriesProvider;

  public CountryValidator(
      CachedAvailableCountriesProvider cachedAvailableCountriesProvider) {
    this.cachedAvailableCountriesProvider = cachedAvailableCountriesProvider;
  }

  @Override
  public boolean isValid(String country, ConstraintValidatorContext constraintValidatorContext) {
    if (country == null) {
      return false;
    }

    Map<String, AvailableCountryDtoItem> availableCountries = cachedAvailableCountriesProvider.getCachedAvailableCountries();

    if (availableCountries == null) {
      return false;
    }

    return availableCountries.containsKey(country);
  }
}
