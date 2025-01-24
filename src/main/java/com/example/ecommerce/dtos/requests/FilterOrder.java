package com.example.ecommerce.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FilterOrder {

    private String customerEmail;
    private String customerNumber;
    private Long productId;
}
