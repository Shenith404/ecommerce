package com.example.ecommerce.controller;

import com.example.ecommerce.dto.reponse.ApiResponseDTO;
import com.example.ecommerce.service.interfaces.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    // increase stock
    @PostMapping("/{id}/stock/add")
    public ResponseEntity<ApiResponseDTO<Void>> addStock(@PathVariable String id, @RequestParam int amount) {
        inventoryService.increaseStock(id, amount);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Stock increased successfully")
                .success(true)
                .build()
        );
    }
    // decrease stock
    @PostMapping("/{id}/stock/remove")
    public ResponseEntity<ApiResponseDTO<Void>> removeStock(@PathVariable String id, @RequestParam int amount) {
        inventoryService.decreaseStock(id, amount);
        return ResponseEntity.ok(ApiResponseDTO.<Void>builder()
                .message("Stock decreased successfully")
                .success(true)
                .build()
        );
    }
}
