package com.example.demo.model.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {

    @NotEmpty
    private String customerId;

    @NotEmpty
    private String assetName;

    @NotNull
    private String side;

    @Min(1)
    private Double size;

    @Min(0)
    private Double price;

}
