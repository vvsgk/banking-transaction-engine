package com.vvsgk.banking_transaction_engine.accounts.service;
import com.vvsgk.banking_transaction_engine.accounts.dto.*;
import com.vvsgk.banking_transaction_engine.accounts.entity.*;
import com.vvsgk.banking_transaction_engine.accounts.repository.AccountRepository;
import com.vvsgk.banking_transaction_engine.common.NotFoundException;
import com.vvsgk.banking_transaction_engine.customers.entity.*;
import com.vvsgk.banking_transaction_engine.customers.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.*;
import java.time.Instant;

@Service @Transactional
public class AccountService {
 private final AccountRepository accounts; private final CustomerService customers;
 public AccountService(AccountRepository accounts, CustomerService customers) { this.accounts=accounts; this.customers=customers; }
 public AccountResponse create(CreateAccountRequest request) {
   Customer customer=customers.getRequired(request.customerId());
   BankAccount account=new BankAccount(); account.setCustomer(customer); account.setType(request.type()); account.setCurrency(request.currency());
   account.setStatus(AccountStatus.ACTIVE); account.setCurrentBalance(BigDecimal.ZERO.setScale(4)); account.setAvailableBalance(BigDecimal.ZERO.setScale(4));
   account.setCreatedAt(Instant.now()); account.setUpdatedAt(Instant.now()); account=accounts.saveAndFlush(account);
   account.setAccountNumber(String.format("ACC%010d", account.getId())); return map(accounts.saveAndFlush(account));
 }
 @Transactional(readOnly=true) public AccountResponse get(String number) { return map(getRequired(number)); }
 public AccountResponse updateStatus(String number, AccountStatus status) { BankAccount a=getRequired(number); a.setStatus(status); a.setUpdatedAt(Instant.now()); return map(a); }
 @Transactional(readOnly=true) public BankAccount getRequired(String number) { return accounts.findByAccountNumber(number).orElseThrow(()->new NotFoundException("Account not found: "+number)); }
 public AccountResponse map(BankAccount a) { return new AccountResponse(a.getAccountNumber(), a.getCustomer().getCustomerId(), a.getType().name(), a.getCurrency(), a.getStatus().name(), a.getCurrentBalance(), a.getAvailableBalance()); }
}
