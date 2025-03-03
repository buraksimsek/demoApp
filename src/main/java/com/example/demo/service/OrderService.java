package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Asset;
import com.example.demo.entity.Order;
import com.example.demo.model.enums.Side;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final AssetService assetService;

    private final OrderRepository orderRepository;

    @Transactional
    public Order createOrder(String customerId, String assetName, Side side, Double size, Double price) {
        // check if there is enough usable size
        if (side == Side.BUY) {
            // We need enough TRY for this purchase
            Asset tryAsset = assetService.findAsset(customerId, "TRY");
            if (tryAsset == null || tryAsset.getUsableSize() < (price * size)) {
                throw new IllegalArgumentException("Not enough TRY to buy " + assetName);
            }
            // Decrease TRY
            assetService.incrementUsableSize(tryAsset, -(price * size));

        } else if (side == Side.SELL) {
            // We need enough usable shares of 'assetName'
            Asset sellingAsset = assetService.findAsset(customerId, assetName);
            if (sellingAsset == null || sellingAsset.getUsableSize() < size) {
                throw new IllegalArgumentException("Not enough shares to sell " + assetName);
            }
            // Decrease asset’s usable size
            assetService.incrementUsableSize(sellingAsset, -size);
        }

        Order order = Order.builder()
            .customerId(customerId)
            .assetName(assetName)
            .orderSide(side)
            .size(size)
            .price(price)
            .status(Status.PENDING)
            .createDate(LocalDateTime.now())
            .build();

        return orderRepository.save(order);
    }

    public List<Order> listOrders(String customerId, LocalDateTime startDate, LocalDateTime endDate) {
        // If date range is provided, filter by that, else return all.
        if (startDate != null && endDate != null) {
            return orderRepository.findByCustomerIdAndCreateDateBetween(customerId, startDate, endDate);
        } else {
            return orderRepository.findByCustomerId(customerId);
        }
    }

    @Transactional
    public void cancelOrder(Long orderId) {
        // only cancel if pending
        Order order = orderRepository.findByIdAndStatus(orderId, Status.PENDING);
        if (order == null) {
            throw new IllegalArgumentException("Order not found or not in PENDING status");
        }
        // revert asset or TRY
        if (order.getOrderSide() == Side.BUY) {
            // revert the TRY asset
            Asset tryAsset = assetService.findAsset(order.getCustomerId(), "TRY");
            double refund = order.getSize() * order.getPrice();
            assetService.incrementUsableSize(tryAsset, refund);
        } else {
            // revert the asset’s usable size
            Asset sellingAsset = assetService.findAsset(order.getCustomerId(), order.getAssetName());
            assetService.incrementUsableSize(sellingAsset, order.getSize());
        }

        order.setStatus(Status.CANCELED);
        orderRepository.save(order);
    }

    @Transactional
    public void matchOrder(Long orderId) {
        Order order = orderRepository.findByIdAndStatus(orderId, Status.PENDING);
        if (order == null) {
            throw new IllegalArgumentException("Order not found or not in PENDING status");
        }

        if (order.getOrderSide() == Side.BUY) {
            // user should get the asset
            Asset asset = assetService.findAsset(order.getCustomerId(), order.getAssetName());
            if (asset == null) {
                // create if not existing
                asset = Asset.builder()
                    .customerId(order.getCustomerId())
                    .assetName(order.getAssetName())
                    .size(0.0)
                    .usableSize(0.0)
                    .build();
            }
            // Increase total and usable size
            asset.setSize(asset.getSize() + order.getSize());
            asset.setUsableSize(asset.getUsableSize() + order.getSize());
            assetService.saveAsset(asset);
        } else {
            // SELL -> user is receiving TRY in exchange
            Asset tryAsset = assetService.findAsset(order.getCustomerId(), "TRY");
            if (tryAsset == null) {
                throw new IllegalArgumentException("Customer does not have TRY asset to receive sale amount");
            }
            double proceeds = order.getSize() * order.getPrice();
            assetService.incrementUsableSize(tryAsset, proceeds);
        }

        order.setStatus(Status.MATCHED);
        orderRepository.save(order);
    }

}

