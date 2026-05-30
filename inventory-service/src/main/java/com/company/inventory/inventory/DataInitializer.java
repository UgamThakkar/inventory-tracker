package com.company.inventory.inventory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.math.BigDecimal; // Import BigDecimal

@Component
public class DataInitializer implements CommandLineRunner {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public DataInitializer(ProductRepository productRepository, InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("🏁 STARTING DATA INITIALIZER RUN METHOD...");

        try {
            // 1. Clear out stale entries safely (Inventory first due to foreign key constraints!)
            inventoryRepository.deleteAll();
            productRepository.deleteAll();

            System.out.println("🔄 Stale database tables cleared out successfully.");

            // 2. Create the Product record
            Product laptop = new Product();
            laptop.setName("Pro Gaming Laptop 15");
            laptop.setSku("TECH-LAP-001");
            laptop.setPrice(new java.math.BigDecimal("1499.99"));

            System.out.println("💾 Attempting to save Product to PostgreSQL...");
            Product savedProduct = productRepository.save(laptop);
            System.out.println("✅ Product saved successfully! ID assigned: " + savedProduct.getId());

            // 3. Create the Inventory sheet linked to that product
            Inventory laptopInventory = new Inventory();
            laptopInventory.setProduct(savedProduct);
            laptopInventory.setAvailableQuantity(50);
            laptopInventory.setReservedQuantity(0);

            System.out.println("💾 Attempting to save Inventory sheet to PostgreSQL...");
            inventoryRepository.save(laptopInventory);

            System.out.println("🚀 DATABASE SEEDING COMPLETE: Added SKU 'TECH-LAP-001' with 50 units.");

        } catch (Exception e) {
            System.err.println("❌ ERROR DETECTED DURING DATABASE SEEDING!");
            System.err.println("Exception Type: " + e.getClass().getName());
            System.err.println("Error Message: " + e.getMessage());
            e.printStackTrace(); // This prints out the full trail of where it failed
        }
    }
}