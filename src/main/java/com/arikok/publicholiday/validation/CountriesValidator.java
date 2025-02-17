package com.arikok.publicholiday.validation;

import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class CountriesValidator implements ConstraintValidator<ValidCountries, List<String>> {


  private final CachedAvailableCountriesProvider cachedAvailableCountriesProvider;

  public CountriesValidator(
      CachedAvailableCountriesProvider cachedAvailableCountriesProvider) {
    this.cachedAvailableCountriesProvider = cachedAvailableCountriesProvider;
  }

  @Override
  public boolean isValid(List<String> countries, ConstraintValidatorContext context) {
    if (countries == null || countries.size() > 10) {
      return false;
    }

    Map<String, ?> availableCountries = cachedAvailableCountriesProvider.getCachedAvailableCountries();

    if (availableCountries == null) {
      return false;
    }

    for (String country : countries) {
      if (!availableCountries.containsKey(country)) {
        return false;
      }
    }

    return true;

  }
}
