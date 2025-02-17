package com.arikok.publicholiday.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arikok.publicholiday.config.TestContainerHelper;
import com.arikok.publicholiday.service.countries.AvailableCountryDtoItem;
import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
import com.arikok.publicholiday.service.lastcelebrated.FindLastCelebratedHolidaysQuery;
import com.arikok.publicholiday.service.lastcelebrated.LastCelebratedHolidaysService;
import com.arikok.publicholiday.service.lastcelebrated.LastCelebratedHolidaysSummaryDto;
import java.time.LocalDate;
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
class LastCelebratedHolidaysControllerTest {

  static {
    TestContainerHelper.initializeRedisContainer();
  }

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private LastCelebratedHolidaysService lastCelebratedHolidaysService;

  @MockitoBean
  private CachedAvailableCountriesProvider cachedAvailableCountriesProvider;

  @Test
  void getLastCelebratedHolidaysByCountryAndCount_validRequest_returnsExpectedSummary() {

    String country = "US";
    int count = 1;

    List<LastCelebratedHolidaysSummaryDto> expectedResponse = List.of(
        new LastCelebratedHolidaysSummaryDto(LocalDate.of(2025, 1, 1), "New Year's Day")
    );

    FindLastCelebratedHolidaysQuery query = new FindLastCelebratedHolidaysQuery(count, country);
    when(lastCelebratedHolidaysService.getLastCelebratedHolidays(query))
        .thenReturn(Mono.just(expectedResponse));

    Map<String, AvailableCountryDtoItem> availableCountries = Map.of("US",
        new AvailableCountryDtoItem("US", "United States"), "NL",
        new AvailableCountryDtoItem("NL", "Netherlands"));

    when(cachedAvailableCountriesProvider.getCachedAvailableCountries()).thenReturn(
        availableCountries
    );

    webTestClient.get()
        .uri("/v1/holiday/last/{country}/{count}", country, count)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0].date").isEqualTo("2025-01-01")
        .jsonPath("$[0].name").isEqualTo("New Year's Day");

    ArgumentCaptor<FindLastCelebratedHolidaysQuery> queryCaptor =
        ArgumentCaptor.forClass(FindLastCelebratedHolidaysQuery.class);
    verify(lastCelebratedHolidaysService).getLastCelebratedHolidays(queryCaptor.capture());
    FindLastCelebratedHolidaysQuery capturedQuery = queryCaptor.getValue();
    // Adjust these assertions if your query object exposes different getters.
    assert capturedQuery.holidayCount() == count;
    assert capturedQuery.countryCode().equals(country);
  }

  @Test
  void getLastCelebratedHolidaysByCountryAndCount_invalidCountry_returnsBadRequest() {
    String invalidCountry = "XX";
    int count = 5;
    webTestClient.get()
        .uri("/v1/holiday/last/{country}/{count}", invalidCountry, count)
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void getLastCelebratedHolidaysByCountryAndCount_invalidCount_returnsBadRequest() {
    String country = "US";
    int invalidCount = 0;
    webTestClient.get()
        .uri("/v1/holiday/last/{country}/{count}", country, invalidCount)
        .exchange()
        .expectStatus().isBadRequest();
  }
}
