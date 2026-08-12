package com.vvsgk.banking_transaction_engine.accounts.controller;
import com.vvsgk.banking_transaction_engine.accounts.dto.*;
import com.vvsgk.banking_transaction_engine.accounts.entity.AccountStatus;
import com.vvsgk.banking_transaction_engine.accounts.service.AccountService;
import com.vvsgk.banking_transaction_engine.transactions.dto.TransactionResponse;
import com.vvsgk.banking_transaction_engine.transactions.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
@RestController @RequestMapping("/accounts")
public class AccountController {
 private final AccountService service; private final TransactionService transactions; public AccountController(AccountService service, TransactionService transactions){this.service=service;this.transactions=transactions;}
 @PostMapping public AccountResponse create(@Valid @RequestBody CreateAccountRequest request){return service.create(request);}
 @GetMapping("/{number}") public AccountResponse get(@PathVariable String number){return service.get(number);}
 @PatchMapping("/{number}/status") public AccountResponse status(@PathVariable String number,@RequestParam AccountStatus status){return service.updateStatus(number,status);}
 @GetMapping("/{number}/transactions") public java.util.List<TransactionResponse> history(@PathVariable String number){return transactions.history(number);}
}
