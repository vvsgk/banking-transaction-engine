package com.vvsgk.banking_transaction_engine.customers.controller;

import com.vvsgk.banking_transaction_engine.customers.dto.CustomerRegistrationRequest;
import com.vvsgk.banking_transaction_engine.customers.dto.CustomerResponse;
import com.vvsgk.banking_transaction_engine.customers.entity.CustomerStatus;
import com.vvsgk.banking_transaction_engine.customers.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController @RequestMapping("/customers")
public class CustomerController {
    private final CustomerService service;
    public CustomerController(CustomerService service) { this.service = service; }
    @PostMapping({"", "/register"}) public CustomerResponse register(@Valid @RequestBody CustomerRegistrationRequest request) { return service.register(request); }
    @GetMapping public List<CustomerResponse> getCustomers() { return service.getCustomers(); }
    @PatchMapping("/{id}/status") public CustomerResponse updateStatus(@PathVariable Long id, @RequestParam CustomerStatus status) { return service.updateStatus(id, status); }
}
