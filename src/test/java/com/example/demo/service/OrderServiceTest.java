package com.example.demo.service;

import com.example.demo.entity.Asset;
import com.example.demo.entity.Order;
import com.example.demo.model.enums.Side;
import com.example.demo.model.enums.Status;
import com.example.demo.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private AssetService assetService;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void createOrder_ShouldCreateBuyOrder_WhenSufficientFundsAvailable() {
        // Arrange
        String customerId = "12345";
        String assetName = "AAPL";
        Side side = Side.BUY;
        Double size = 10.0;
        Double price = 50.0;

        Asset tryAsset = Asset.builder()
            .customerId(customerId)
            .assetName("TRY")
            .usableSize(1000.0)
            .build();

        when(assetService.findAsset(customerId, "TRY")).thenReturn(tryAsset);

        Order expectedOrder = Order.builder()
            .customerId(customerId)
            .assetName(assetName)
            .orderSide(side)
            .size(size)
            .price(price)
            .status(Status.PENDING)
            .createDate(LocalDateTime.now())
            .build();

        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        // Act
        Order actualOrder = orderService.createOrder(customerId, assetName, side, size, price);

        // Assert
        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder.getCustomerId()).isEqualTo(customerId);
        assertThat(actualOrder.getAssetName()).isEqualTo(assetName);
        assertThat(actualOrder.getOrderSide()).isEqualTo(side);
        assertThat(actualOrder.getSize()).isEqualTo(size);
        assertThat(actualOrder.getPrice()).isEqualTo(price);
        assertThat(actualOrder.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    public void createOrder_ShouldThrowException_WhenFundsAreInsufficientForBuy() {
        // Arrange
        String customerId = "12345";
        String assetName = "AAPL";
        Side side = Side.BUY;
        Double size = 10.0;
        Double price = 500.0;

        Asset tryAsset = Asset.builder()
            .customerId(customerId)
            .assetName("TRY")
            .usableSize(1000.0)
            .build();

        when(assetService.findAsset(customerId, "TRY")).thenReturn(tryAsset);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(customerId, assetName, side, size, price))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not enough TRY to buy " + assetName);
    }

    @Test
    public void createOrder_ShouldCreateSellOrder_WhenSufficientAssetsAvailable() {
        // Arrange
        String customerId = "12345";
        String assetName = "AAPL";
        Side side = Side.SELL;
        Double size = 10.0;
        Double price = 50.0;

        Asset sellingAsset = Asset.builder()
            .customerId(customerId)
            .assetName(assetName)
            .usableSize(20.0)
            .build();

        when(assetService.findAsset(customerId, assetName)).thenReturn(sellingAsset);

        Order expectedOrder = Order.builder()
            .customerId(customerId)
            .assetName(assetName)
            .orderSide(side)
            .size(size)
            .price(price)
            .status(Status.PENDING)
            .createDate(LocalDateTime.now())
            .build();

        when(orderRepository.save(any(Order.class))).thenReturn(expectedOrder);

        // Act
        Order actualOrder = orderService.createOrder(customerId, assetName, side, size, price);

        // Assert
        assertThat(actualOrder).isNotNull();
        assertThat(actualOrder.getCustomerId()).isEqualTo(customerId);
        assertThat(actualOrder.getAssetName()).isEqualTo(assetName);
        assertThat(actualOrder.getOrderSide()).isEqualTo(side);
        assertThat(actualOrder.getSize()).isEqualTo(size);
        assertThat(actualOrder.getPrice()).isEqualTo(price);
        assertThat(actualOrder.getStatus()).isEqualTo(Status.PENDING);
    }

    @Test
    public void createOrder_ShouldThrowException_WhenAssetsAreInsufficientForSell() {
        // Arrange
        String customerId = "12345";
        String assetName = "AAPL";
        Side side = Side.SELL;
        Double size = 50.0;
        Double price = 10.0;

        Asset sellingAsset = Asset.builder()
            .customerId(customerId)
            .assetName(assetName)
            .usableSize(20.0)
            .build();

        when(assetService.findAsset(customerId, assetName)).thenReturn(sellingAsset);

        // Act & Assert
        assertThatThrownBy(() -> orderService.createOrder(customerId, assetName, side, size, price))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Not enough shares to sell " + assetName);
    }

}