package com.example.demo.repository;

import com.example.demo.entity.Customer;

public interface CustomerRepository {

    Customer findByCustomerId(String customerId);

}
