package com.company.inventory.inventory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;
    private final  ProductRepository productRepository;

    public InventoryService(InventoryRepository inventoryRepository, ProductRepository productRepository){
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void deductStock(String sku, Integer quantity) {
        // Call the custom query that locks the row on disk right at the start of the transaction
        Inventory inventory = inventoryRepository.findByProductSkuWithLock(sku)
                .orElseThrow(() -> new RuntimeException("Inventory records missing for item: " + sku));

        if (inventory.getAvailableQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock available for SKU: " + sku);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantity);
        inventoryRepository.save(inventory);
    }
}
