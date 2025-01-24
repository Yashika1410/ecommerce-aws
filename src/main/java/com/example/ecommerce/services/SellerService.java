package com.example.ecommerce.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.CreateSellerRequest;
import com.example.ecommerce.entities.Seller;
import com.example.ecommerce.repositories.SellerRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Service
@RequiredArgsConstructor
@Slf4j
public class SellerService {
    private final SellerRepository sellerRepository;
    public Seller getSellerById(Long sellerId) {
      return sellerRepository.findById(sellerId)
              .orElseThrow(() -> new ResourceNotFoundException("Seller not found"));
    }

    public Seller saveSeller(CreateSellerRequest seller) {
        return sellerRepository.save(Seller.builder()
                .name(seller.getName())
                .description(seller.getDescription())
                .address(seller.getAddress())
                .number(seller.getNumber())
                .build());
    }

    public Seller updateSeller(Long id, CreateSellerRequest seller) {
        Seller sellerToUpdate = getSellerById(id);
        sellerToUpdate.setName(seller.getName());
        sellerToUpdate.setDescription(seller.getDescription());
        sellerToUpdate.setAddress(seller.getAddress());
        sellerToUpdate.setNumber(seller.getNumber());
        return sellerRepository.save(sellerToUpdate);
    }

    public void deleteSeller(Long id) {
      Seller seller = getSellerById(id);
      sellerRepository.delete(seller);
    }

    public Page<Seller> getAllSellers(int page, int size) {
        return sellerRepository.findAll(PageRequest.of(page, size));
    }
    
}
