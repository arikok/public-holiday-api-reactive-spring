package com.arikok.publicholiday.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arikok.publicholiday.config.TestContainerHelper;
import com.arikok.publicholiday.service.countries.AvailableCountryDtoItem;
import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysCountQuery;
import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysService;
import com.arikok.publicholiday.service.nonweekend.NonWeekendHolidaysSummaryDto;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

@AutoConfigureMockMvc
@SpringBootTest
class NonWeekendHolidaysControllerTest {

  static {
    TestContainerHelper.initializeRedisContainer();
  }

  @Autowired
  private WebTestClient webTestClient;
  @MockitoBean
  private NonWeekendHolidaysService nonWeekendHolidaysService;
  @MockitoBean
  private CachedAvailableCountriesProvider cachedAvailableCountriesProvider;

  @Test
  void getNonWeekendHolidaysByPath_validRequest_returnsExpectedMapping() {
    int year = 2025;
    String country1 = "US";
    String country2 = "NL";
    List<NonWeekendHolidaysSummaryDto> expectedResponse = List.of(
        new NonWeekendHolidaysSummaryDto("US", 3),
        new NonWeekendHolidaysSummaryDto("NL", 1)
    );
    NonWeekendHolidaysCountQuery query = new NonWeekendHolidaysCountQuery(year,
        Arrays.asList(country1, country2));
    when(nonWeekendHolidaysService.getNonWeekendHolidaysCounts(query))
        .thenReturn(Mono.just(expectedResponse));

    Map<String, AvailableCountryDtoItem> availableCountries = Map.of("US",
        new AvailableCountryDtoItem("US", "United States"),
        "NL",
        new AvailableCountryDtoItem("NL", "Netherlands"));

    when(cachedAvailableCountriesProvider.getCachedAvailableCountries()).thenReturn(
        availableCountries
    );

    webTestClient.get()
        .uri("/v1/holiday/non-weekend/{year}/{country1}/{country2}", year, country1, country2)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].countryCode").isEqualTo("US")
        .jsonPath("$[0].nonWeekendHolidayCount").isEqualTo(3);

    ArgumentCaptor<NonWeekendHolidaysCountQuery> queryCaptor =
        ArgumentCaptor.forClass(NonWeekendHolidaysCountQuery.class);
    verify(nonWeekendHolidaysService).getNonWeekendHolidaysCounts(queryCaptor.capture());
    NonWeekendHolidaysCountQuery capturedQuery = queryCaptor.getValue();
    assert capturedQuery.year() == year;
    assert capturedQuery.countries().equals(Arrays.asList(country1, country2));
  }

  @Test
  void getNonWeekendHolidaysByPath_invalidYear_returnsBadRequest() {
    int invalidYear = -2025;
    webTestClient.get()
        .uri("/v1/holiday/non-weekend/{year}/{country1}/{country2}", invalidYear, "US", "CA")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void getNonWeekendHolidaysByPath_invalidCountry_returnsBadRequest() {
    int year = 2025;
    String invalidCountry = "XX";  // Assuming "XX" is not valid
    webTestClient.get()
        .uri("/v1/holiday/non-weekend/{year}/{country1}/{country2}", year, invalidCountry, "CA")
        .exchange()
        .expectStatus().isBadRequest();
  }

}