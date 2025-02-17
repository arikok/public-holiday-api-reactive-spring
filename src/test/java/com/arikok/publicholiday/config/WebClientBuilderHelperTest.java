package com.arikok.publicholiday.config;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.reactive.function.client.WebClient;

class WebClientBuilderHelperTest {

  private WebClient.Builder webClientBuilder;
  private WebClientBuilderHelper webClientBuilderHelper;

  @BeforeEach
  void setUp() {
    webClientBuilder = Mockito.mock(WebClient.Builder.class);
    WebClient dummyWebClient = Mockito.mock(WebClient.class);

    // chaining
    when(webClientBuilder.baseUrl(anyString())).thenReturn(webClientBuilder);
    when(webClientBuilder.build()).thenReturn(dummyWebClient);

    webClientBuilderHelper = new WebClientBuilderHelper(webClientBuilder);
  }

  @Test
  void testBuildWebClientCallsBaseUrlAndBuild() {
    String expectedBaseUrl = "http://example.com";

    WebClient webClient = webClientBuilderHelper.buildWebClient(expectedBaseUrl);
    
    verify(webClientBuilder).baseUrl(eq(expectedBaseUrl));

    verify(webClientBuilder).build();

  }

  @Test
  void testBuildWebClientWithNullBaseUrl() {
    assertThrows(IllegalArgumentException.class, () -> {
      webClientBuilderHelper.buildWebClient(null);
    });
  }
}
