package com.vvsgk.banking_transaction_engine.customers.repository;

import com.vvsgk.banking_transaction_engine.customers.entity.Customer;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class InMemoryCustomerRepository implements CustomerRepository {

    private final Map<Long, Customer> customers = new HashMap<>();

    @Override
    public Customer save(Customer customer) {

        customers.put(customer.getCustomerId(), customer);

        return customer;
    }

    @Override
    public Optional<Customer> findById(Long id) {

        return Optional.ofNullable(customers.get(id));
    }

    @Override
    public List<Customer> findAll() {

        return new ArrayList<>(customers.values());
    }
}