package com.example.ecommerce.dtos.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderRequest {
    @NotNull(message = "Product Id is required")
    private Long productId;
    @NotEmpty(message = "Customer email is required")
    private String customerEmail;
    @NotEmpty(message = "Customer number is required")
    private String customerNumber;
}
