package com.vvsgk.banking_transaction_engine.transactions.entity;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal; import java.time.Instant;
@Entity @Table(name="bank_transactions") @Getter @Setter @NoArgsConstructor
public class BankTransaction {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,updatable=false) private String transactionId;
 @Column(nullable=false,unique=true,updatable=false) private String reference;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TransactionType type;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private TransactionStatus status;
 @Column(nullable=false,precision=19,scale=4) private BigDecimal amount;
 @Column(nullable=false,length=3) private String currency;
 @Column(nullable=false) private String initiatorType; @Column(nullable=false) private String initiatorId;
 @Column(unique=true) private String idempotencyKey; @Column private String requestFingerprint;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="reversal_of_id") private BankTransaction reversalOf;
 @Column(nullable=false,updatable=false) private Instant createdAt;
}
