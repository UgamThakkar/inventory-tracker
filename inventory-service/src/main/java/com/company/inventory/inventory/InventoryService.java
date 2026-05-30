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
    public boolean deductStock(String sku, int quantityToDeduct){
        if(quantityToDeduct<=0){
            throw new IllegalArgumentException("Deduction Quantity must be greater than one");
        }

        Optional<Inventory> inventoryOPT = inventoryRepository.findByProductSkuWithLock(sku);

        if(inventoryOPT.isEmpty()){
            return false;
        }

        Inventory inventory = inventoryOPT.get();

        if(inventory.getAvailableQuantity() < quantityToDeduct){
            throw new IllegalStateException("Insufficient stock available for SKU:" + sku);
        }

        inventory.setAvailableQuantity(inventory.getAvailableQuantity() - quantityToDeduct);

        inventoryRepository.save(inventory);
        return true;
    }
}
