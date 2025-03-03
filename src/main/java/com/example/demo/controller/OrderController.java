package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.model.enums.Side;
import com.example.demo.model.requests.CreateOrderRequest;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public Order createOrder(@Valid @RequestBody CreateOrderRequest request) {
        return orderService.createOrder(
            request.getCustomerId(),
            request.getAssetName(),
            Side.valueOf(request.getSide()),
            request.getSize(),
            request.getPrice()
        );
    }

    @GetMapping
    public List<Order> listOrders(
        @RequestParam(name="customerId", required = false) String customerId,
        @RequestParam(name="startDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(name="endDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        return orderService.listOrders(customerId, startDate, endDate);
    }

    @DeleteMapping("/{orderId}")
    public void cancelOrder(@PathVariable Long orderId) {
        orderService.cancelOrder(orderId);
    }

    //bunu admin controller'a alabilirsin.
    @PatchMapping("/{orderId}/match")
    public void matchOrder(@PathVariable Long orderId) {
        orderService.matchOrder(orderId);
    }

}

