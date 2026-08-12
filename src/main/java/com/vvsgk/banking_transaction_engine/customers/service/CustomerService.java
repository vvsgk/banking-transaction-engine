package com.vvsgk.banking_transaction_engine.customers.service;

import com.vvsgk.banking_transaction_engine.common.NotFoundException;
import com.vvsgk.banking_transaction_engine.customers.dto.CustomerRegistrationRequest;
import com.vvsgk.banking_transaction_engine.customers.dto.CustomerResponse;
import com.vvsgk.banking_transaction_engine.customers.entity.*;
import com.vvsgk.banking_transaction_engine.customers.repository.CustomerRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    private final CustomerRepository repository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public CustomerService(CustomerRepository repository) { this.repository = repository; }

    public CustomerResponse register(CustomerRegistrationRequest request) {
        if (repository.existsByEmail(request.getEmail())) throw new IllegalArgumentException("Email is already registered");
        Customer customer = new Customer();
        customer.setFirstName(request.getFirstName()); customer.setLastName(request.getLastName());
        customer.setEmail(request.getEmail()); customer.setPhone(request.getPhone());
        customer.setPassword(passwordEncoder.encode(request.getPassword()));
        customer.setRole(CustomerRole.CUSTOMER); customer.setStatus(CustomerStatus.ACTIVE);
        customer.setCreatedAt(Instant.now()); customer.setUpdatedAt(Instant.now());
        customer = repository.saveAndFlush(customer);
        customer.setCustomerNumber(String.format("CUS%06d", customer.getCustomerId()));
        return map(repository.saveAndFlush(customer));
    }
    @Transactional(readOnly = true) public List<CustomerResponse> getCustomers() { return repository.findAll().stream().map(this::map).toList(); }
    @Transactional(readOnly = true) public Customer getRequired(Long id) { return repository.findById(id).orElseThrow(() -> new NotFoundException("Customer not found: " + id)); }
    public CustomerResponse updateStatus(Long id, CustomerStatus status) {
        Customer customer = getRequired(id); customer.setStatus(status); customer.setUpdatedAt(Instant.now()); return map(customer);
    }
    private CustomerResponse map(Customer c) {
        CustomerResponse r = new CustomerResponse(); r.setCustomerId(c.getCustomerId()); r.setCustomerNumber(c.getCustomerNumber());
        r.setFirstName(c.getFirstName()); r.setLastName(c.getLastName()); r.setEmail(c.getEmail()); r.setPhone(c.getPhone());
        r.setRole(c.getRole().name()); r.setStatus(c.getStatus().name()); return r;
    }
}
