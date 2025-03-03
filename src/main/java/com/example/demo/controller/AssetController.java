package com.example.demo.controller;

import java.util.List;

import com.example.demo.entity.Asset;
import com.example.demo.service.AssetService;
import lombok.RequiredArgsConstructor;
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
    public List<Asset> listAssets(@PathVariable String customerId) {
        return assetService.listAssetsByCustomer(customerId);
    }
}

