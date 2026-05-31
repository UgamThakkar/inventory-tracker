package com.company.inventory.inventory;

import jakarta.validation.Valid;
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
    public ResponseEntity<String> deductStock(@Valid @RequestBody InventoryRequestDTO request){
        inventoryService.deductStock(request.getSku(), request.getQuantity());
        return ResponseEntity.ok("Stock deducted Successfully for SKU: " + request.getSku());
    }
}
