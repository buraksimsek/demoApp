package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Asset;
import com.example.demo.model.response.ApiResponse;
import com.example.demo.service.AssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    @GetMapping("/{customerId}")
    public ResponseEntity<ApiResponse<List<Asset>>> listAssets(@PathVariable String customerId) {

        try {
            List<Asset> assets = assetService.listAssetsByCustomer(customerId);
            return ResponseEntity.ok(new ApiResponse<>(assets));

        } catch (Exception ex) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(ex.getMessage()));

        }

    }

}

