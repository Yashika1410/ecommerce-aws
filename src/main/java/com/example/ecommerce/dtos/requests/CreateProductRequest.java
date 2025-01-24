package com.example.ecommerce.dtos.requests;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder.Default;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateProductRequest {
    @NotBlank(message = "Name is mandatory")
    private String name;
    @NotBlank(message = "Description is mandatory")
    private String description;
    @Default
    private int stocks=0;
    @Default
    private double price=0.0;

    private String uuid;
    private List<String> tags;
    @NotNull(message = "Seller Id is mandatory")
    private Long sellerId;
}
