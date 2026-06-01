package com.company.inventory.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @InjectMocks
    private InventoryService inventoryService;

    private Product testProduct;
    private Inventory testInventory;

    @BeforeEach
    void setUp(){
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setSku("TECH-LAP-001");
        testProduct.setName("Pro Gaming Laptop 15");
        testProduct.setPrice(new BigDecimal("1499.99"));

        testInventory = new Inventory();
        testInventory.setId(1L);
        testInventory.setProduct(testProduct);
        testInventory.setAvailableQuantity(50);
        testInventory.setReservedQuantity(0);
    }

    @Test
    void deductStock_Success(){
        when(ProductRepository.findBySku("TECH-LAP-001")).thenReturn(Optional.of(testProduct));
        when(inventoryRepository.findByProduct(testProduct)).thenReturn(Optional.of(testInventory));

        inventoryService.deductStock("TECH-LAP-001", 5);

        assertEquals(45, testInventory.getAvailableQuantity());

        verify(inventoryRepository, times(1).save(testInventory));
    }

    @Test
    void deductStock_InsufficientStock_ThrowsException() {
        when(productRepository.findBySku("TECH-LAP-001")).thenReturn(Optional.of(testProduct));
        when(inventoryRepository.findByProduct(testProduct)).thenReturn(Optional.of(testInventory));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            inventoryService.deductStock("TECH-LAP-001", 100);
        });

        assertEquals("Insufficient stock available for SKU: TECH-LAP-001", exception.getMessage());

        verify(inventoryRepository, never()).save(any(Inventory.class));
    }
}
