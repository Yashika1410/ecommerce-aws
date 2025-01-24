package com.example.ecommerce.services;


import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.common.exception.DataValidationException;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.CreateProductRequest;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.Seller;
import com.example.ecommerce.repositories.ProductRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final SellerService sellerService;

    // Get product by id
    public Product getProductById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
    }

    // Save product
    public Product saveProduct(CreateProductRequest product) {
        Seller seller = sellerService.getSellerById(product.getSellerId());
        checkUuid(product.getUuid());
        return productRepository.save(Product.builder()
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .stocks(product.getStocks())
                .tags(product.getTags())
                .uuid(product.getUuid())
                .seller(seller)
                .build());
    }

    public Product updateProduct(Long id, CreateProductRequest product) {
        Product productToUpdate = getProductById(id);
        Seller seller = sellerService.getSellerById(product.getSellerId());
        productToUpdate.setName(product.getName());
        productToUpdate.setPrice(product.getPrice());   
        productToUpdate.setDescription(product.getDescription());
        if (StringUtils.isNotBlank(productToUpdate.getUuid()) && !productToUpdate.getUuid().equals(product.getUuid()))
            checkUuid(product.getUuid());
        else if(StringUtils.isBlank(productToUpdate.getUuid()))
            checkUuid(productToUpdate.getUuid());
        productToUpdate.setUuid(product.getUuid());
        productToUpdate.setStocks(product.getStocks());
        productToUpdate.setTags(product.getTags());
        productToUpdate.setSeller(seller);
        return productRepository.save(productToUpdate);
    }
    public Page<Product> getAllProducts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable);
    }

    public void deleteProduct(Long id) {
        Product product = getProductById(id);
        productRepository.delete(product);
    }
    private void checkUuid(String uuid) {
        if (StringUtils.isNotBlank(uuid) && productRepository.existsByUuid(uuid))
                throw new DataValidationException("Duplicate entry of Uuid it should be unique");
    }
    
}
