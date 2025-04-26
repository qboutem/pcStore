package com.hromenko.computerperipherals.repository;

import com.hromenko.computerperipherals.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
