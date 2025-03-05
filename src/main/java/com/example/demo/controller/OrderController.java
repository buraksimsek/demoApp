package com.example.demo.controller;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Order;
import com.example.demo.model.enums.Side;
import com.example.demo.model.requests.CreateOrderRequest;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiResponse<Order>> createOrder(@Valid @RequestBody CreateOrderRequest request) {
        try {
            Order newOrder = orderService.createOrder(
                request.getCustomerId(),
                request.getAssetName(),
                Side.valueOf(request.getSide()),
                request.getSize(),
                request.getPrice()
            );

            return ResponseEntity.ok(new ApiResponse<>(newOrder));

        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ex.getMessage()));
        }

    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> listOrders(
        @RequestParam(name = "customerId", required = false) String customerId,
        @RequestParam(name = "startDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) {
        try {
            List<Order> orders = orderService.listOrders(customerId, startDate, endDate);
            return ResponseEntity.ok(new ApiResponse<>(orders));
        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ex.getMessage()));
        }

    }

    @DeleteMapping("/{orderId}")
    public ResponseEntity<ApiResponse<Void>> cancelOrder(@PathVariable Long orderId) {
        try {
            orderService.cancelOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ex.getMessage()));
        }

    }

    @PatchMapping("/{orderId}/match")
    public ResponseEntity<ApiResponse<Void>> matchOrder(@PathVariable Long orderId) {
        try {
            orderService.matchOrder(orderId);
            return ResponseEntity.ok().build();
        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ex.getMessage()));
        }
    }

}

