package com.example.demo.controller;

import com.example.demo.entity.Asset;
import com.example.demo.repository.AssetRepository;
import com.example.demo.service.AssetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private AssetService assetService;

    @InjectMocks
    private AssetController assetController;

    @BeforeEach
    void setUp() {
        MockMvcBuilders.standaloneSetup(assetController).build();
    }

    @Test
    @DisplayName("Test: List assets for a valid customer ID with results")
    void testListAssetsForValidCustomerWithResults() throws Exception {

        String customerId = "123";
        List<Asset> assets = Arrays.asList(
            Asset.builder().id(1L).build(),
            Asset.builder().id(2L).build()
        );

        when(assetService.listAssetsByCustomer(customerId)).thenReturn(assets);

        mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();

        mockMvc.perform(get("/api/assets/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @DisplayName("Test: List assets for a valid customer ID with no results")
    void testListAssetsForValidCustomerWithNoResults() throws Exception {
        String customerId = "123";
        when(assetService.listAssetsByCustomer(customerId)).thenReturn(List.of());

        mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();

        mockMvc.perform(get("/api/assets/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    @DisplayName("Test: List assets for an invalid customer ID")
    void testListAssetsForInvalidCustomer() throws Exception {
        String customerId = "invalid";

        when(assetService.listAssetsByCustomer(customerId)).thenReturn(List.of());

        mockMvc = MockMvcBuilders.standaloneSetup(assetController).build();

        mockMvc.perform(get("/api/assets/{customerId}", customerId)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.length()").value(0));
    }

}