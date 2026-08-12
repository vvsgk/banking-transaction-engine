package com.vvsgk.banking_transaction_engine.transactions.dto;
import java.math.BigDecimal; import java.time.Instant; import java.util.List;
public record TransactionResponse(String transactionId, String reference, String type, String status, BigDecimal amount, String currency, String reversalOf, Instant createdAt, List<LedgerEntryResponse> entries) {}
