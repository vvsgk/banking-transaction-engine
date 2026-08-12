package com.vvsgk.banking_transaction_engine.accounts.entity;
import com.vvsgk.banking_transaction_engine.customers.entity.Customer;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
@Entity @Table(name="accounts") @Getter @Setter @NoArgsConstructor
public class BankAccount {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY, optional=false) @JoinColumn(name="customer_id") private Customer customer;
 @Column(nullable=false, unique=true) private String accountNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private AccountType type;
 @Column(nullable=false, length=3) private String currency;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private AccountStatus status;
 @Column(nullable=false, precision=19, scale=4) private BigDecimal currentBalance;
 @Column(nullable=false, precision=19, scale=4) private BigDecimal availableBalance;
 @Version private Long version;
 @Column(nullable=false, updatable=false) private Instant createdAt;
 @Column(nullable=false) private Instant updatedAt;
}
