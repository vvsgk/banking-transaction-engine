package com.vvsgk.banking_transaction_engine.transactions.dto;
import java.math.BigDecimal;
public record LedgerEntryResponse(String accountNumber, String ledgerAccount, String entryType, BigDecimal amount, String currency) {}
