package com.vvsgk.banking_transaction_engine;

import com.vvsgk.banking_transaction_engine.accounts.dto.*;
import com.vvsgk.banking_transaction_engine.accounts.service.AccountService;
import com.vvsgk.banking_transaction_engine.customers.dto.*;
import com.vvsgk.banking_transaction_engine.customers.service.CustomerService;
import com.vvsgk.banking_transaction_engine.transactions.dto.*;
import com.vvsgk.banking_transaction_engine.transactions.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceIntegrationTests {
 @Autowired CustomerService customers; @Autowired AccountService accounts; @Autowired TransactionService transactions;
 @Test void creditTransferIdempotencyAndReversalBalance() {
  var c1=customers.register(customer("one@example.com")); var c2=customers.register(customer("two@example.com"));
  var a1=accounts.create(new CreateAccountRequest(c1.getCustomerId(), com.vvsgk.banking_transaction_engine.accounts.entity.AccountType.SAVINGS,"USD"));
  var a2=accounts.create(new CreateAccountRequest(c2.getCustomerId(), com.vvsgk.banking_transaction_engine.accounts.entity.AccountType.CURRENT,"USD"));
  transactions.adminCredit(new OperationRequest(a1.accountNumber(),new BigDecimal("100.00"),"credit-1","ADMIN","tester"));
  var transfer=transactions.transfer(new TransferRequest(a1.accountNumber(),a2.accountNumber(),new BigDecimal("25.00"),"transfer-1","CUSTOMER",c1.getCustomerNumber()));
  assertEquals(new BigDecimal("75.0000"),accounts.get(a1.accountNumber()).availableBalance());
  assertEquals(new BigDecimal("25.0000"),accounts.get(a2.accountNumber()).availableBalance());
  assertEquals(transfer.transactionId(), transactions.transfer(new TransferRequest(a1.accountNumber(),a2.accountNumber(),new BigDecimal("25.00"),"transfer-1","CUSTOMER",c1.getCustomerNumber())).transactionId());
  assertThrows(IllegalStateException.class,()->transactions.transfer(new TransferRequest(a1.accountNumber(),a2.accountNumber(),new BigDecimal("26.00"),"transfer-1","CUSTOMER",c1.getCustomerNumber())));
  transactions.reverse(transfer.transactionId(),"reverse-1","ADMIN","tester");
  assertEquals(new BigDecimal("100.0000"),accounts.get(a1.accountNumber()).availableBalance());
  assertEquals(new BigDecimal("0.0000"),accounts.get(a2.accountNumber()).availableBalance());
 }
 private CustomerRegistrationRequest customer(String email) { CustomerRegistrationRequest r=new CustomerRegistrationRequest();r.setFirstName("Test");r.setLastName("User");r.setEmail(email);r.setPhone("123");r.setPassword("password");return r; }
}
