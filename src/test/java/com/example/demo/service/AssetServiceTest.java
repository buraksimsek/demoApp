package com.example.demo.service;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class AssetServiceTest {

    @Test
    void testIncrementUsableSizeWithPositiveDelta() {
        // Arrange
        AssetRepository assetRepository = Mockito.mock(AssetRepository.class);
        AssetService assetService = new AssetService(assetRepository);

        Asset asset = Asset.builder()
            .id(1L)
            .usableSize(100.0)
            .build();

        double sizeDelta = 50.0;

        // Act
        assetService.incrementUsableSize(asset, sizeDelta);

        // Assert
        assertEquals(150.0, asset.getUsableSize());
        verify(assetRepository, times(1)).save(asset);
    }

    @Test
    void testIncrementUsableSizeWithNegativeDelta() {
        // Arrange
        AssetRepository assetRepository = Mockito.mock(AssetRepository.class);
        AssetService assetService = new AssetService(assetRepository);

        Asset asset = Asset.builder()
            .id(1L)
            .usableSize(100.0)
            .build();

        double sizeDelta = -30.0;

        // Act
        assetService.incrementUsableSize(asset, sizeDelta);

        // Assert
        assertEquals(70.0, asset.getUsableSize());
        verify(assetRepository, times(1)).save(asset);
    }

    @Test
    void testIncrementUsableSizeWithZeroDelta() {
        // Arrange
        AssetRepository assetRepository = Mockito.mock(AssetRepository.class);
        AssetService assetService = new AssetService(assetRepository);

        Asset asset = Asset.builder()
            .id(1L)
            .usableSize(100.0)
            .build();

        double sizeDelta = 0.0;

        // Act
        assetService.incrementUsableSize(asset, sizeDelta);

        // Assert
        assertEquals(100.0, asset.getUsableSize());
        verify(assetRepository, times(1)).save(asset);
    }

}