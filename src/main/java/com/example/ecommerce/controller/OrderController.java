package com.example.ecommerce.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.ecommerce.dtos.requests.FilterOrder;
import com.example.ecommerce.dtos.requests.OrderRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.services.OrderService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController extends ApiRestHandler {
    private final OrderService orderService;

    @PostMapping
    public ResponseWrapper placeOrder(@Valid @RequestBody OrderRequest order) {
        // Implementation here
        return ResponseWrapper.convertObjectToResponseMapperForSuccess(orderService.placeOrder(order), "Order placed successfully");
    }

   
            @PostMapping("/orders/filter")
            public ResponseWrapper getOrderDetailsByFilter(@RequestBody FilterOrder filterRequest, @RequestParam(value = "page", defaultValue = "0") int page,
                    @RequestParam(value = "size", defaultValue = "10") int size) {
                // Implementation here
                return ResponseWrapper.convertObjectToResponseMapperForSuccess(orderService.getAllOrders(page,size,filterRequest), "Order details fetched successfully");
            }
}
