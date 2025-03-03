package com.example.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AssetService {

    private final AssetRepository assetRepository;

    public List<Asset> listAssetsByCustomer(String customerId) {
        return assetRepository.findByCustomerId(customerId);
    }

    public Asset findAsset(String customerId, String assetName) {
        return assetRepository.findByCustomerIdAndAssetName(customerId, assetName);
    }

    public void saveAsset(Asset asset) {
        assetRepository.save(asset);
    }

    public void incrementUsableSize(Asset asset, double sizeDelta) {
        double newUsableSize = asset.getUsableSize() + sizeDelta;
        asset.setUsableSize(newUsableSize);
        assetRepository.save(asset);
    }

}

