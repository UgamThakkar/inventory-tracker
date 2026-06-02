package com.company.inventory.inventory;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(InventoryController.class)
public class InventoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private InventoryService inventoryService;

    @Test
    void whenInvalidQuantity_thenReturn400() throws Exception {
        // Arrange: Create a bad payload with an illegal quantity of 0
        InventoryRequestDTO badRequest = new InventoryRequestDTO();
        badRequest.setSku("TECH-LAP-001");
        badRequest.setQuantity(0); // This violates @Min(1)!

        // Act & Assert: Send the JSON payload to the endpoint and check if it gets blocked
        mockMvc.perform(post("/api/inventory/deduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(badRequest)))
                .andExpect(status().isBadRequest()); // Expects 400 Bad Request
    }
}