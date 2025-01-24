package com.example.ecommerce.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.FilterOrder;
import com.example.ecommerce.dtos.requests.OrderRequest;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.repositories.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductService productService;

    public Order placeOrder(OrderRequest orderRequest) {
        // Implementation here
        Product product = productService.getProductById(orderRequest.getProductId());
        return orderRepository.save(Order.builder()
                .product(product)
                .customerEmail(orderRequest.getCustomerEmail())
                .customerNumber(orderRequest.getCustomerNumber())
                .build());

    }

    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    public Page<Order> getAllOrders(int page, int size, FilterOrder filterOrder) {
        Pageable pageable = PageRequest.of(page, size);
        return orderRepository.findAllByFilter(filterOrder.getCustomerEmail(),
                filterOrder.getCustomerNumber(), 
                filterOrder.getProductId(), 
                pageable);
    }

}
