package com.example.ecommerce.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entities.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE "
            + "(:customerEmail IS NULL OR o.customerEmail = :customerEmail) AND "
            + "(:customerNumber IS NULL OR o.customerNumber = :customerNumber) AND "
            + "(:productId IS NULL OR o.product.id = :productId)")
    Page<Order> findAllByFilter(String customerEmail, String customerNumber, Long productId, Pageable pageable);  
    
}
