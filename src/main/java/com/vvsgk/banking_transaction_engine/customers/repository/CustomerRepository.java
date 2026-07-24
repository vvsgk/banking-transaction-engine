package com.vvsgk.banking_transaction_engine.customers.repository;

import com.vvsgk.banking_transaction_engine.customers.entity.Customer;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository {

    Customer save(Customer customer);

    Optional<Customer> findById(Long id);

    List<Customer> findAll();
}