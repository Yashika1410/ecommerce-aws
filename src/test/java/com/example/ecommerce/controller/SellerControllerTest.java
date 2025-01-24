
package com.example.ecommerce.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.ecommerce.dtos.requests.CreateSellerRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.entities.Seller;
import com.example.ecommerce.services.SellerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
public class SellerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private SellerService sellerService;

    @InjectMocks
    private SellerController sellerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sellerController).build();
    }

    @Test
    public void testGetSellerById() throws Exception {

        when(sellerService.getSellerById(anyLong())).thenReturn(new Seller());

        mockMvc.perform(get("/api/v1/sellers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Seller retrieved successfully"))
                .andExpect(jsonPath("$.success").value("true"));
    }

    @Test
    public void testGetAllSellers() throws Exception {

        when(sellerService.getAllSellers(any(int.class), any(int.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/api/v1/sellers")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Sellers retrieved successfully"));
    }

    @Test
    public void testCreateSeller() throws Exception {
        CreateSellerRequest request = new CreateSellerRequest();


        when(sellerService.saveSeller(any(CreateSellerRequest.class))).thenReturn(Seller.builder().build());

        mockMvc.perform(post("/api/v1/sellers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Seller created successfully"));
    }

    @Test
    public void testUpdateSeller() throws Exception {
        CreateSellerRequest request = new CreateSellerRequest();

        when(sellerService.updateSeller(anyLong(), any(CreateSellerRequest.class))).thenReturn(Seller.builder().build());

        mockMvc.perform(put("/api/v1/sellers/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Seller updated successfully"));
    }

    @Test
    public void testDeleteSeller() throws Exception {
        mockMvc.perform(delete("/api/v1/sellers/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Seller deleted successfully"));
    }
}