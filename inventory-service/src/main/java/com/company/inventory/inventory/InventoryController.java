package com.company.inventory.inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    @PostMapping("/deduct")
    public ResponseEntity<String> deductInventory(@RequestParam String sku, @RequestParam int quantity){

        try{
            boolean success = inventoryService.deductStock(sku, quantity);

            if(success){
                return ResponseEntity.ok("Stock Deducted Successfully for SKU:" + sku);
            }else{
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Product or Inventory record not found for SKU: " + sku);
            }
        }catch (IllegalArgumentException | IllegalStateException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
