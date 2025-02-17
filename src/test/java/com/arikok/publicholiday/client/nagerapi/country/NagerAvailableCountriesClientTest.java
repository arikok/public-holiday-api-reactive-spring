package com.arikok.publicholiday.client.nagerapi.country;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.arikok.publicholiday.client.nagerapi.NagerApiProperties;
import com.arikok.publicholiday.config.WebClientBuilderHelper;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.json.BasicJsonTester;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

class NagerAvailableCountriesClientTest {

  private final BasicJsonTester json = new BasicJsonTester(this.getClass());
  private MockWebServer mockWebServer;

  private NagerAvailableCountriesClient client;

  @BeforeEach
  void setupMockWebServer() {
    // mockServer
    mockWebServer = new MockWebServer();
    String mockServerUrl = mockWebServer.url("/").url().toString();

    //create WebClient
    WebClient.Builder webClientBuilder = Mockito.mock(WebClient.Builder.class);
    WebClient webClient = WebClient.create(mockServerUrl);
    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(webClient);
    WebClientBuilderHelper webClientBuilderHelper = new WebClientBuilderHelper(webClientBuilder);

    NagerApiProperties properties = new NagerApiProperties();
    properties.setBaseUrl(mockServerUrl);

    when(webClientBuilderHelper.buildWebClient(mockServerUrl)).thenReturn(webClient);

    client = new NagerAvailableCountriesClient(webClientBuilderHelper, properties);

  }

  @Test
  void makesTheCorrectRequest() throws InterruptedException {
    mockWebServer.enqueue(
        new MockResponse().setResponseCode(200)
            .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .setBody(getJson("nager-available-countries-response.json"))
    );

    client.fetchAvailableCountries().block();

    RecordedRequest request = mockWebServer.takeRequest();

    assertThat(request.getMethod()).isEqualTo("GET");
    assertThat(request.getPath()).isEqualTo(
        "/api/v3/AvailableCountries");
  }

  private String getJson(String path) {
    try {
      InputStream jsonStream = this.getClass().getClassLoader().getResourceAsStream(path);
      assert jsonStream != null;
      return new String(jsonStream.readAllBytes());
    } catch (IOException ex) {
      throw new RuntimeException(ex);
    }
  }

}
