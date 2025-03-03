package com.example.demo.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.model.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    // e.g. find orders by customerId and date range
    List<Order> findByCustomerIdAndCreateDateBetween(String customerId, LocalDateTime start, LocalDateTime end);

    List<Order> findByCustomerId(String customerId);

    // for cancel use-cases, etc.
    Order findByIdAndStatus(Long id, Status status);

}
