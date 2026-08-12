package com.vvsgk.banking_transaction_engine.transactions.controller;
import com.vvsgk.banking_transaction_engine.transactions.dto.*; import com.vvsgk.banking_transaction_engine.transactions.service.TransactionService;
import jakarta.validation.Valid; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController @RequestMapping("/transactions")
public class TransactionController {
 private final TransactionService service; public TransactionController(TransactionService service){this.service=service;}
 @PostMapping("/admin-credits") public TransactionResponse credit(@Valid @RequestBody OperationRequest request){return service.adminCredit(request);}
 @PostMapping("/transfers") public TransactionResponse transfer(@Valid @RequestBody TransferRequest request){return service.transfer(request);}
 @PostMapping("/{id}/reversals") public TransactionResponse reverse(@PathVariable String id,@RequestHeader("Idempotency-Key") String key,@RequestHeader("Initiator-Type") String type,@RequestHeader("Initiator-Id") String initiator){return service.reverse(id,key,type,initiator);}
 @GetMapping("/{id}") public TransactionResponse get(@PathVariable String id){return service.get(id);}
}
