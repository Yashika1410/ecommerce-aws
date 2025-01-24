package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.requests.CreateProductRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.services.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;




@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController extends ApiRestHandler{
    private final ProductService productService;

    // create a new product
    @PostMapping
    public ResponseWrapper createProduct(@Valid @RequestBody CreateProductRequest product) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(productService.saveProduct(product),"Product created successfully");
    }
    // update a product
    @PutMapping("/{id}")
    public ResponseWrapper updateProduct(@PathVariable Long id,@Valid  @RequestBody CreateProductRequest product) {
        
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(productService.updateProduct(id,product),"Product updated successfully");
    }
    // delete a product
    @DeleteMapping("/{id}")
    public ResponseWrapper deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(null,"Product deleted successfully");
    }
    // get all products
    @GetMapping
    public ResponseWrapper getMethodName(@RequestParam(name = "page",defaultValue = "0") int page, @RequestParam(name = "size",defaultValue = "10") int size) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(productService.getAllProducts(page, size),"Products retrieved successfully");
    }
    
    // get a product by id
    @GetMapping("/{id}")
    public ResponseWrapper getMethodName(@PathVariable Long id) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(productService.getProductById(id),"Product retrieved successfully");
    }
    
}
