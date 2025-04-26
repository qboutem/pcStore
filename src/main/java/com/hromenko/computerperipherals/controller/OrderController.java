package com.hromenko.computerperipherals.controller;

import com.hromenko.computerperipherals.dto.OrderRequest;
import com.hromenko.computerperipherals.model.Order;
import com.hromenko.computerperipherals.model.OrderItem;
import com.hromenko.computerperipherals.model.Peripheral;
import com.hromenko.computerperipherals.repository.OrderRepository;
import com.hromenko.computerperipherals.repository.PeripheralRepository;
import com.hromenko.computerperipherals.repository.OrderItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping()
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PeripheralRepository peripheralRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();

        Order order = new Order();
        order.setTotalPrice(request.getTotalPrice());
        order.setCreatedAt(LocalDateTime.now());
        order.setUser(username);
        order = orderRepository.save(order);

        final Order savedOrder = order;

        List<OrderItem> orderItems = request.getItems().stream()
                .map(id -> {
                    Peripheral peripheral = peripheralRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Peripheral not found: " + id));

                    OrderItem orderItem = new OrderItem();
                    orderItem.setOrder(savedOrder);
                    orderItem.setPeripheral(peripheral);
                    return orderItem;
                })
                .collect(Collectors.toList());

        orderItemRepository.saveAll(orderItems);
        order.setOrderItems(orderItems);

        return ResponseEntity.ok(order);
    }

}

