package com.example.demo.entity;

import java.time.LocalDateTime;

import com.example.demo.model.enums.Side;
import com.example.demo.model.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerId;

    private String assetName;

    @Enumerated(EnumType.STRING)
    private Side orderSide;

    private Double size;

    private Double price;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime createDate;

}
