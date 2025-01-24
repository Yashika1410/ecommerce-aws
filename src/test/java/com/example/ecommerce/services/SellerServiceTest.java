package com.example.ecommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.CreateSellerRequest;
import com.example.ecommerce.entities.Seller;
import com.example.ecommerce.repositories.SellerRepository;

@ExtendWith(MockitoExtension.class)
public class SellerServiceTest {

    @Mock
    private SellerRepository sellerRepository;


    private SellerService sellerService;

    @BeforeEach
    public void setUp() {
       sellerService = new SellerService(sellerRepository);
    }

    @Test
    public void testGetSellerById_Success() {
        Seller seller = new Seller();
        seller.setId(1L);
        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        Seller foundSeller = sellerService.getSellerById(1L);

        assertEquals(seller.getId(), foundSeller.getId());
    }

    @Test
    public void testGetSellerById_NotFound() {
        when(sellerRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            sellerService.getSellerById(1L);
        });
    }

    @Test
    public void testSaveSeller() {
        CreateSellerRequest request = new CreateSellerRequest();
        request.setName("Test Seller");
        request.setDescription("Test Description");
        request.setAddress("Test Address");
        request.setNumber("1234567890");

        Seller seller = Seller.builder()
                .name(request.getName())
                .description(request.getDescription())
                .address(request.getAddress())
                .number(request.getNumber())
                .build();

        when(sellerRepository.save(seller)).thenReturn(seller);

        Seller savedSeller = sellerService.saveSeller(request);

        assertEquals(request.getName(), savedSeller.getName());
        assertEquals(request.getDescription(), savedSeller.getDescription());
        assertEquals(request.getAddress(), savedSeller.getAddress());
        assertEquals(request.getNumber(), savedSeller.getNumber());
    }

    @Test
    public void testUpdateSeller() {
        CreateSellerRequest request = new CreateSellerRequest();
        request.setName("Updated Seller");
        request.setDescription("Updated Description");
        request.setAddress("Updated Address");
        request.setNumber("0987654321");

        Seller existingSeller = new Seller();
        existingSeller.setId(1L);
        existingSeller.setName("Old Seller");
        existingSeller.setDescription("Old Description");
        existingSeller.setAddress("Old Address");
        existingSeller.setNumber("1234567890");

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(existingSeller));
        when(sellerRepository.save(existingSeller)).thenReturn(existingSeller);

        Seller updatedSeller = sellerService.updateSeller(1L, request);

        assertEquals(request.getName(), updatedSeller.getName());
        assertEquals(request.getDescription(), updatedSeller.getDescription());
        assertEquals(request.getAddress(), updatedSeller.getAddress());
        assertEquals(request.getNumber(), updatedSeller.getNumber());
    }

    @Test
    public void testDeleteSeller() {
        Seller seller = new Seller();
        seller.setId(1L);

        when(sellerRepository.findById(1L)).thenReturn(Optional.of(seller));

        sellerService.deleteSeller(1L);

        verify(sellerRepository).delete(seller);
    }
}
