package com.vvsgk.banking_transaction_engine.transactions.repository;
import com.vvsgk.banking_transaction_engine.transactions.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface LedgerEntryRepository extends JpaRepository<LedgerEntry,Long> { List<LedgerEntry> findByTransactionTransactionIdOrderById(String transactionId); List<LedgerEntry> findByAccountAccountNumberOrderByIdDesc(String accountNumber); }
