package com.vvsgk.banking_transaction_engine.transactions.dto;
import jakarta.validation.constraints.*; import java.math.BigDecimal;
public record OperationRequest(@NotBlank String destinationAccountNumber, @NotNull @DecimalMin(value="0.0001") BigDecimal amount, @NotBlank String idempotencyKey, @NotBlank String initiatorType, @NotBlank String initiatorId) {}
