package com.arikok.publicholiday.client.nagerapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class NagerApiProperties {

  @Value("${nager.api.base-url}")
  private String baseUrl;

  public NagerApiProperties() {

  }

  public NagerApiProperties(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public String getBaseUrl() {
    return baseUrl;
  }

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }
}
