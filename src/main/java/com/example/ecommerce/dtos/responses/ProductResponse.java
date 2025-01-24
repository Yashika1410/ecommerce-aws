package com.example.ecommerce.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductResponse {
    private long id;
    private String name;
    private String description;
    private int stocks;
    private double price;
    private List<String> tags;
    private List<DocumentMappingResponse> docs;
}
