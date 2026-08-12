package com.vvsgk.banking_transaction_engine.accounts.repository;
import com.vvsgk.banking_transaction_engine.accounts.entity.BankAccount;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface AccountRepository extends JpaRepository<BankAccount, Long> {
 Optional<BankAccount> findByAccountNumber(String accountNumber);
 @Lock(LockModeType.PESSIMISTIC_WRITE)
 @Query("select a from BankAccount a join fetch a.customer where a.id in :ids order by a.id")
 List<BankAccount> lockByIds(@Param("ids") Collection<Long> ids);
}
