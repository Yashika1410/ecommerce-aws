
package com.example.ecommerce.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import com.example.ecommerce.dtos.requests.CreateProductRequest;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.services.ProductService;
import com.example.ecommerce.utils.ParserUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;




public class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    public void testCreateProduct() throws Exception {
        CreateProductRequest request = CreateProductRequest.builder().description("description").name("name").sellerId(Long.valueOf(0)).price(10.0).build();
        when(productService.saveProduct(any(CreateProductRequest.class))).thenReturn(Product.builder().build());

        mockMvc.perform(post("/api/v1/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParserUtils.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product created successfully"));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        CreateProductRequest request = CreateProductRequest.builder().description("description").name("name")
                .sellerId(Long.valueOf(0)).price(10.0).build();
        when(productService.updateProduct(anyLong(), any(CreateProductRequest.class))).thenReturn(Product.builder().build());

        mockMvc.perform(put("/api/v1/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParserUtils.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product updated successfully"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        mockMvc.perform(delete("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product deleted successfully"));
    }

    @Test
    public void testGetAllProducts() throws Exception {
       
        when(productService.getAllProducts(any(int.class), any(int.class))).thenReturn(new PageImpl<>(java.util.Arrays.asList(Product.builder().build()),(Pageable)PageRequest.of(0, 10),1));  

        mockMvc.perform(get("/api/v1/products")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Products retrieved successfully"));
    }

    @Test
    public void testGetProductById() throws Exception {
        when(productService.getProductById(anyLong())).thenReturn(Product.builder().build());

        mockMvc.perform(get("/api/v1/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Product retrieved successfully"));
    }
}