package com.vvsgk.banking_transaction_engine.transactions.repository;
import com.vvsgk.banking_transaction_engine.transactions.entity.TransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;
public interface TransactionEventRepository extends JpaRepository<TransactionEvent,Long> {}
