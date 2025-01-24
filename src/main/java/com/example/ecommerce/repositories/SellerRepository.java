package com.example.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Seller;
@Repository
public interface SellerRepository extends CrudRepository<Seller, Long> {

    Page<Seller> findAll(Pageable pageable);
    
}
