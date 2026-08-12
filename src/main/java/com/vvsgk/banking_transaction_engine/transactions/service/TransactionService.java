package com.vvsgk.banking_transaction_engine.transactions.service;
import com.vvsgk.banking_transaction_engine.accounts.entity.*; import com.vvsgk.banking_transaction_engine.accounts.repository.AccountRepository;
import com.vvsgk.banking_transaction_engine.common.NotFoundException; import com.vvsgk.banking_transaction_engine.customers.entity.CustomerStatus;
import com.vvsgk.banking_transaction_engine.transactions.dto.*; import com.vvsgk.banking_transaction_engine.transactions.entity.*; import com.vvsgk.banking_transaction_engine.transactions.repository.*;
import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional;
import java.math.*; import java.time.Instant; import java.util.*;

@Service @Transactional
public class TransactionService {
 private final AccountRepository accounts; private final TransactionRepository transactions; private final LedgerEntryRepository entries; private final TransactionEventRepository events;
 public TransactionService(AccountRepository a, TransactionRepository t, LedgerEntryRepository l, TransactionEventRepository e){accounts=a;transactions=t;entries=l;events=e;}
 public TransactionResponse adminCredit(OperationRequest r){ return post(null,r.destinationAccountNumber(),r.amount(),r.idempotencyKey(),r.initiatorType(),r.initiatorId(),TransactionType.ADMIN_CREDIT,null); }
 public TransactionResponse transfer(TransferRequest r){ return post(r.sourceAccountNumber(),r.destinationAccountNumber(),r.amount(),r.idempotencyKey(),r.initiatorType(),r.initiatorId(),TransactionType.TRANSFER,null); }
 public TransactionResponse reverse(String id, String key, String initiatorType, String initiatorId) {
   BankTransaction original=transactions.findByTransactionId(id).orElseThrow(()->new NotFoundException("Transaction not found: "+id));
   if(transactions.existsByReversalOfId(original.getId())) throw new IllegalStateException("Transaction has already been reversed");
   List<LedgerEntry> originalEntries=entries.findByTransactionTransactionIdOrderById(id);
   LedgerEntry debit=originalEntries.stream().filter(e->e.getEntryType()==EntryType.DEBIT && e.getAccount()!=null).findFirst().orElse(null);
   LedgerEntry credit=originalEntries.stream().filter(e->e.getEntryType()==EntryType.CREDIT && e.getAccount()!=null).findFirst().orElseThrow(()->new IllegalStateException("Invalid original ledger"));
   TransactionResponse reversal = post(credit.getAccount().getAccountNumber(), debit==null?null:debit.getAccount().getAccountNumber(), original.getAmount(), key, initiatorType, initiatorId, TransactionType.REVERSAL, original);
   original.setStatus(TransactionStatus.REVERSED);
   return reversal;
 }
 private TransactionResponse post(String sourceNo,String destinationNo,BigDecimal raw,String key,String it,String iid,TransactionType type,BankTransaction reversalOf){
   String fingerprint=type+"|"+sourceNo+"|"+destinationNo+"|"+raw.stripTrailingZeros()+"|"+it+"|"+iid;
   Optional<BankTransaction> old=transactions.findByIdempotencyKey(key); if(old.isPresent()){ if(!fingerprint.equals(old.get().getRequestFingerprint())) throw new IllegalStateException("Idempotency key was reused with a different request"); return map(old.get()); }
   BigDecimal amount=raw.setScale(4,RoundingMode.UNNECESSARY);
   List<String> nums=new ArrayList<>(); if(sourceNo!=null)nums.add(sourceNo); if(destinationNo!=null)nums.add(destinationNo);
   List<BankAccount> locked=accounts.lockByIds(nums.stream().map(n->accounts.findByAccountNumber(n).orElseThrow(()->new NotFoundException("Account not found: "+n)).getId()).toList());
   Map<String,BankAccount> byNo=new HashMap<>(); locked.forEach(a->byNo.put(a.getAccountNumber(),a)); BankAccount source=sourceNo==null?null:byNo.get(sourceNo); BankAccount dest=destinationNo==null?null:byNo.get(destinationNo);
   if(dest==null && source==null) throw new IllegalArgumentException("An account is required"); if(dest!=null) validate(dest); if(source!=null){ validate(source); if(dest!=null && !source.getCurrency().equals(dest.getCurrency())) throw new IllegalArgumentException("Cross-currency transfers are not supported"); if(source.getAvailableBalance().compareTo(amount)<0) throw new IllegalStateException("Insufficient available balance"); source.setCurrentBalance(source.getCurrentBalance().subtract(amount)); source.setAvailableBalance(source.getAvailableBalance().subtract(amount)); }
   if(dest!=null){ dest.setCurrentBalance(dest.getCurrentBalance().add(amount)); dest.setAvailableBalance(dest.getAvailableBalance().add(amount)); } Instant now=Instant.now(); if(source!=null)source.setUpdatedAt(now); if(dest!=null)dest.setUpdatedAt(now);
   String currency=dest!=null?dest.getCurrency():source.getCurrency();
   BankTransaction tx=new BankTransaction(); tx.setTransactionId(UUID.randomUUID().toString()); tx.setReference("TXN-"+UUID.randomUUID().toString().substring(0,8).toUpperCase()); tx.setType(type); tx.setStatus(TransactionStatus.COMPLETED); tx.setAmount(amount); tx.setCurrency(currency); tx.setInitiatorType(it); tx.setInitiatorId(iid); tx.setIdempotencyKey(key); tx.setRequestFingerprint(fingerprint); tx.setReversalOf(reversalOf); tx.setCreatedAt(now); tx=transactions.save(tx);
   if(source==null) { entry(tx,null,"BANK_FUNDING",EntryType.DEBIT,amount,currency); } else entry(tx,source,"CUSTOMER",EntryType.DEBIT,amount,currency); if(dest==null) entry(tx,null,"BANK_FUNDING",EntryType.CREDIT,amount,currency); else entry(tx,dest,"CUSTOMER",EntryType.CREDIT,amount,currency);
   TransactionEvent event=new TransactionEvent(); event.setTransaction(tx);event.setEventType(type+"_COMPLETED");event.setPayload("{\"amount\":\""+amount+"\"}");event.setCreatedAt(now);events.save(event); return map(tx);
 }
 private void validate(BankAccount a){if(a.getStatus()!=AccountStatus.ACTIVE||a.getCustomer().getStatus()!=CustomerStatus.ACTIVE)throw new IllegalStateException("Account and customer must be ACTIVE");}
 private void entry(BankTransaction tx,BankAccount a,String la,EntryType et,BigDecimal amount,String currency){LedgerEntry e=new LedgerEntry();e.setTransaction(tx);e.setAccount(a);e.setLedgerAccount(la);e.setEntryType(et);e.setAmount(amount);e.setCurrency(currency);entries.save(e);}
 @Transactional(readOnly=true) public TransactionResponse get(String id){return map(transactions.findByTransactionId(id).orElseThrow(()->new NotFoundException("Transaction not found: "+id)));}
 @Transactional(readOnly=true) public List<TransactionResponse> history(String n){return entries.findByAccountAccountNumberOrderByIdDesc(n).stream().map(e->map(e.getTransaction())).distinct().toList();}
 private TransactionResponse map(BankTransaction tx){return new TransactionResponse(tx.getTransactionId(),tx.getReference(),tx.getType().name(),tx.getStatus().name(),tx.getAmount(),tx.getCurrency(),tx.getReversalOf()==null?null:tx.getReversalOf().getTransactionId(),tx.getCreatedAt(),entries.findByTransactionTransactionIdOrderById(tx.getTransactionId()).stream().map(e->new LedgerEntryResponse(e.getAccount()==null?null:e.getAccount().getAccountNumber(),e.getLedgerAccount(),e.getEntryType().name(),e.getAmount(),e.getCurrency())).toList());}
}
