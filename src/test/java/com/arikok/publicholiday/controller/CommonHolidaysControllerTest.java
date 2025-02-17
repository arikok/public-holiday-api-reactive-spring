package com.arikok.publicholiday.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.arikok.publicholiday.service.commonholiday.CommonHolidayQuery;
import com.arikok.publicholiday.service.commonholiday.CommonHolidaysService;
import com.arikok.publicholiday.service.countries.AvailableCountryDtoItem;
import com.arikok.publicholiday.service.countries.CachedAvailableCountriesProvider;
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
class CommonHolidaysControllerTest {

  @Autowired
  private WebTestClient webTestClient;

  @MockitoBean
  private CommonHolidaysService commonHolidaysService;

  @MockitoBean
  private CachedAvailableCountriesProvider cachedAvailableCountriesProvider;

  @Test
  void getCommonHolidaysByPath_validRequest_returnsExpectedMapping() {
    int year = 2025;
    String country1 = "US";
    String country2 = "NL";
    List<Map<String, Map<String, String>>> expectedResponse = List.of(
        Map.of("2025-01-01", Map.of("US", "New Year's Day", "NL", "Nieuwjaarsdag"))
    );

    CommonHolidayQuery cmd = new CommonHolidayQuery(2025, Arrays.asList(country1, country2));
    when(commonHolidaysService.getCommonHolidaysMapping(cmd))
        .thenReturn(Mono.just(expectedResponse));

    Map<String, AvailableCountryDtoItem> availableCountries = Map.of("US",
        new AvailableCountryDtoItem("US", "United States"), "NL",
        new AvailableCountryDtoItem("NL", "Netherlands"));

    when(cachedAvailableCountriesProvider.getCachedAvailableCountries()).thenReturn(
        availableCountries
    );

    webTestClient.get()
        .uri("/v1/holiday/common/{year}/{country1}/{country2}", year, country1, country2)
        .accept(MediaType.APPLICATION_JSON)
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBody()
        .jsonPath("$[0]['2025-01-01'].US").isEqualTo("New Year's Day")
        .jsonPath("$[0]['2025-01-01'].NL").isEqualTo("Nieuwjaarsdag");

    ArgumentCaptor<CommonHolidayQuery> queryCaptor = ArgumentCaptor.forClass(
        CommonHolidayQuery.class);
    verify(commonHolidaysService).getCommonHolidaysMapping(queryCaptor.capture());
    CommonHolidayQuery query = queryCaptor.getValue();
    assert (query.year() == year);
    assert (query.countries().equals(List.of(country1, country2)));
  }

  @Test
  void getCommonHolidaysByPath_invalidYear_returnsBadRequest() {
    int invalidYear = -2025;
    webTestClient.get()
        .uri("/v1/holiday/common/{year}/{country1}/{country2}", invalidYear, "US", "CA")
        .exchange()
        .expectStatus().isBadRequest();
  }

  @Test
  void getCommonHolidaysByPath_invalidCountry_returnsBadRequest() {
    int year = 2025;
    String invalidCountry = "XX";
    webTestClient.get()
        .uri("/v1/holiday/common/{year}/{country1}/{country2}", year, invalidCountry, "CA")
        .exchange()
        .expectStatus().isBadRequest();
  }
}
