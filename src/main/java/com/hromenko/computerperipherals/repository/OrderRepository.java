package com.hromenko.computerperipherals.repository;

import com.hromenko.computerperipherals.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
