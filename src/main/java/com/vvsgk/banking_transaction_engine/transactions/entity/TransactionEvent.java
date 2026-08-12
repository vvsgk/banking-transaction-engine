package com.vvsgk.banking_transaction_engine.transactions.entity;
import jakarta.persistence.*; import lombok.*; import java.time.Instant;
@Entity @Table(name="transaction_events") @Getter @Setter @NoArgsConstructor
public class TransactionEvent { @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id; @ManyToOne(optional=false) @JoinColumn(name="transaction_id") private BankTransaction transaction; @Column(nullable=false) private String eventType; @Column(nullable=false) private String payload; @Column(nullable=false) private Instant createdAt; }
