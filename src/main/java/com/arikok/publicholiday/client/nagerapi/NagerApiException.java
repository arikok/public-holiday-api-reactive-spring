package com.arikok.publicholiday.client.nagerapi;

public class NagerApiException extends RuntimeException {

  public static String CLIENT_ERROR = "Nager API Client 4xx Error!";
  public static String SERVER_ERROR = "Nager API Client 4xx Error!";

  public NagerApiException(String message) {
    super(message);
  }

  public NagerApiException(String message, Throwable cause) {
    super(message, cause);
  }
}
