package com.arikok.publicholiday.controller;

import com.arikok.publicholiday.service.ServiceException;
import jakarta.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);


  @ExceptionHandler(ServiceException.class)
  public ResponseEntity<Map<String, Object>> handleServiceException(ServiceException ex) {
    log.error(ex.getMessage(), ex);
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Service Error");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(
      WebExchangeBindException ex) {
    log.error(ex.getMessage(), ex);
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Validation Error");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Map<String, Object>> handleConstraintViolation(
      ConstraintViolationException ex) {
    log.error(ex.getMessage(), ex);
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Constraint Violation");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
    log.error(ex.getMessage(), ex);
    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("error", "Internal Server Error");
    errorResponse.put("message", ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
