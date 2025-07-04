package com.hromenko.computerperipherals.repository;

import com.hromenko.computerperipherals.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(String email);
}
