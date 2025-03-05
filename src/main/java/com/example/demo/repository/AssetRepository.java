package com.example.demo.repository;

import java.util.List;

import com.example.demo.entity.Asset;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Long> {

    List<Asset> findByCustomerId(String customerId);

    @Lock(LockModeType.OPTIMISTIC)
    Asset findByCustomerIdAndAssetName(String customerId, String assetName);

}
