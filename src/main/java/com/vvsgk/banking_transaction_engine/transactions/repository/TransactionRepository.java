package com.vvsgk.banking_transaction_engine.transactions.repository;
import com.vvsgk.banking_transaction_engine.transactions.entity.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface TransactionRepository extends JpaRepository<BankTransaction,Long> { Optional<BankTransaction> findByTransactionId(String id); Optional<BankTransaction> findByIdempotencyKey(String key); boolean existsByReversalOfId(Long id); }
