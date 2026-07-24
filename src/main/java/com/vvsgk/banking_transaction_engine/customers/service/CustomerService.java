package com.vvsgk.banking_transaction_engine.customers.service;

import com.vvsgk.banking_transaction_engine.customers.dto.CustomerRegistrationRequest;
import com.vvsgk.banking_transaction_engine.customers.dto.CustomerResponse;
import com.vvsgk.banking_transaction_engine.customers.entity.*;
import com.vvsgk.banking_transaction_engine.customers.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class CustomerService {

    private final CustomerRepository repository;

    private final AtomicLong sequence = new AtomicLong(1);

    public CustomerService(CustomerRepository repository) {
        this.repository = repository;
    }

    public CustomerResponse register(CustomerRegistrationRequest request) {

        Long id = sequence.getAndIncrement();

        Customer customer = new Customer();

        customer.setCustomerId(id);
        customer.setCustomerNumber(String.format("CUS%06d", id));

        customer.setFirstName(request.getFirstName());
        customer.setLastName(request.getLastName());

        customer.setEmail(request.getEmail());
        customer.setPhone(request.getPhone());

        customer.setPassword(request.getPassword());

        customer.setRole(CustomerRole.CUSTOMER);

        customer.setStatus(CustomerStatus.ACTIVE);

        customer.setCreatedAt(LocalDateTime.now());

        repository.save(customer);

        return map(customer);
    }

    public List<Customer> getCustomers() {

        return repository.findAll();
    }

    private CustomerResponse map(Customer customer) {

        CustomerResponse response = new CustomerResponse();

        response.setCustomerId(customer.getCustomerId());
        response.setCustomerNumber(customer.getCustomerNumber());

        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());

        response.setEmail(customer.getEmail());
        response.setPhone(customer.getPhone());

        response.setRole(customer.getRole().name());
        response.setStatus(customer.getStatus().name());

        return response;
    }
}