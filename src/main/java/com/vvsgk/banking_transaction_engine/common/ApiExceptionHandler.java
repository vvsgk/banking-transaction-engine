package com.vvsgk.banking_transaction_engine.common;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestControllerAdvice
public class ApiExceptionHandler {
  @ExceptionHandler(NotFoundException.class) ResponseEntity<?> notFound(NotFoundException e) { return error(HttpStatus.NOT_FOUND, e.getMessage()); }
  @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class}) ResponseEntity<?> conflict(RuntimeException e) { return error(HttpStatus.CONFLICT, e.getMessage()); }
  @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class) ResponseEntity<?> validation(Exception e) { return error(HttpStatus.BAD_REQUEST, "Validation failed"); }
  private ResponseEntity<Map<String,String>> error(HttpStatus s, String message) { return ResponseEntity.status(s).body(Map.of("error", s.getReasonPhrase(), "message", message)); }
}
