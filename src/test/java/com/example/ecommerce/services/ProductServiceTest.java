package com.example.ecommerce.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.ecommerce.common.exception.DataValidationException;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.CreateProductRequest;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.entities.Seller;
import com.example.ecommerce.repositories.ProductRepository;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SellerService sellerService;

    private ProductService productService;

    @BeforeEach
    public void setUp() {
        productService = new ProductService(productRepository, sellerService);
    }

    @Test
    public void testGetProductById_ProductExists() {
        Product product = new Product();
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertNotNull(result);
        verify(productRepository).findById(1L);
    }

    @Test
    public void testGetProductById_ProductNotFound() {
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> productService.getProductById(1L));
    }

    @Test
    public void testSaveProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setSellerId(1L);
        request.setUuid("unique-uuid");
        Seller seller = new Seller();
        when(sellerService.getSellerById(any(Long.class))).thenReturn(seller);
        when(productRepository.save(any(Product.class))).thenReturn(new Product());

        Product result = productService.saveProduct(request);

        assertNotNull(result);
        verify(sellerService).getSellerById(1L);
        verify(productRepository).save(any(Product.class));
    }

    @Test
    public void testUpdateProduct() {
        CreateProductRequest request = new CreateProductRequest();
        request.setSellerId(1L);
        request.setUuid("unique-uuid");
        Product product = new Product();
        Seller seller = new Seller();
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));
        when(sellerService.getSellerById(any(Long.class))).thenReturn(seller);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, request);

        assertNotNull(result);
        verify(productRepository).findById(1L);
        verify(sellerService).getSellerById(1L);
        verify(productRepository).save(product);
    }

    @Test
    public void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Product> page = new PageImpl<>(List.of(new Product()));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productService.getAllProducts(0, 10);

        assertNotNull(result);
        verify(productRepository).findAll(pageable);
    }

    @Test
    public void testDeleteProduct() {
        Product product = new Product();
        when(productRepository.findById(any(Long.class))).thenReturn(Optional.of(product));

        productService.deleteProduct(1L);

        verify(productRepository).findById(1L);
        verify(productRepository).delete(product);
    }

    @Test
    public void testCheckUuid_DuplicateUuid() {
        when(productRepository.existsByUuid(any(String.class))).thenReturn(true);

        assertThrows(DataValidationException.class, () -> productService.saveProduct(CreateProductRequest.builder().uuid("unique-uuid").build()));
    }
}
