package com.vvsgk.banking_transaction_engine.transactions.entity;
import com.vvsgk.banking_transaction_engine.accounts.entity.BankAccount;
import jakarta.persistence.*; import lombok.*; import java.math.BigDecimal;
@Entity @Table(name="ledger_entries") @Getter @Setter @NoArgsConstructor
public class LedgerEntry {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="transaction_id") private BankTransaction transaction;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="account_id") private BankAccount account;
 @Column(nullable=false) private String ledgerAccount;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private EntryType entryType;
 @Column(nullable=false,precision=19,scale=4) private BigDecimal amount;
 @Column(nullable=false,length=3) private String currency;
}
