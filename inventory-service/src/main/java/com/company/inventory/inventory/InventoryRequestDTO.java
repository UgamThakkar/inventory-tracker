package com.company.inventory.inventory;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class InventoryRequestDTO {
    @NotBlank(message = "SKU cannot be blank or empty")
    private String sku;

    @NotNull(message = "Quantity is important")
    @Min(value = 1, message = "Deduction quantity must be atleast 1")
    private Integer quantity;
}
