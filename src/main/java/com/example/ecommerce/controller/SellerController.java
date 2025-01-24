package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.requests.CreateSellerRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.services.SellerService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/api/v1/sellers")
@RequiredArgsConstructor
public class SellerController extends ApiRestHandler {

    private final SellerService sellerService;

    @GetMapping("/{id}")
    public ResponseWrapper getSellerById(@PathVariable("id") Long id) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(sellerService.getSellerById(id), "Seller retrieved successfully");
    }

    @GetMapping
    public ResponseWrapper getAllSellers(@RequestParam(name = "page", defaultValue = "0") int page, @RequestParam(name = "size", defaultValue = "10") int size) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(sellerService.getAllSellers(page, size), "Sellers retrieved successfully");
    }

    @PostMapping
    public ResponseWrapper createSeller(@RequestBody CreateSellerRequest createSellerRequest) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(sellerService.saveSeller(createSellerRequest), "Seller created successfully");
    }

    @PutMapping("/{id}")
    public ResponseWrapper updateSeller(@PathVariable String id, @RequestBody CreateSellerRequest entity) {
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(sellerService.updateSeller(Long.parseLong(id), entity), "Seller updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseWrapper deleteSeller(@PathVariable String id) {
        sellerService.deleteSeller(Long.parseLong(id));
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(null, "Seller deleted successfully");
    }
    
    
    
    
}
