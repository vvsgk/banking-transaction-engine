package com.vvsgk.banking_transaction_engine.accounts.dto;
import java.math.BigDecimal;
public record AccountResponse(String accountNumber, Long customerId, String type, String currency, String status, BigDecimal currentBalance, BigDecimal availableBalance) {}
