package com.example.demo;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class IntegrationTest {

    @Autowired
    private AssetRepository assetRepository;

    @Test
    void testAssetRepository() throws InterruptedException {
        // Given

        Asset asset = new Asset(2L, "customerIdTest", "TRY", 100.0, 100.0);
        assetRepository.save(asset);

        // When
        Asset foundAsset = assetRepository.findById(asset.getId()).orElse(null);

        // Then
        Assertions.assertNotNull(foundAsset, "Asset should not be null");
        Assertions.assertEquals("TRY", asset.getAssetName());
    }

}
