package com.vvsgk.banking_transaction_engine.accounts.dto;
import com.vvsgk.banking_transaction_engine.accounts.entity.AccountType;
import jakarta.validation.constraints.*;
public record CreateAccountRequest(@NotNull Long customerId, @NotNull AccountType type, @NotBlank @Pattern(regexp="[A-Z]{3}") String currency) {}
