package com.example.ecommerce.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.ecommerce.common.exception.ResourceNotFoundException;
import com.example.ecommerce.dtos.requests.FilterOrder;
import com.example.ecommerce.dtos.requests.OrderRequest;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.entities.Product;
import com.example.ecommerce.repositories.OrderRepository;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductService productService;


    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderService = new OrderService(orderRepository, productService);
    }

    @Test
    public void testPlaceOrder() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setCustomerEmail("test@example.com");
        orderRequest.setCustomerNumber("1234567890");

        Product product = new Product();
        product.setId(1L);

        when(productService.getProductById(1L)).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(Order.builder()
                .product(product)
                .customerEmail("test@example.com")
                .customerNumber("1234567890")
                .build());

        Order order = orderService.placeOrder(orderRequest);

        assertEquals("test@example.com", order.getCustomerEmail());
        assertEquals("1234567890", order.getCustomerNumber());
        assertEquals(product, order.getProduct());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order foundOrder = orderService.getOrderById(1L);

        assertEquals(1L, foundOrder.getId());
    }

    @Test
    public void testGetOrderByIdNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            orderService.getOrderById(1L);
        });
    }

    @Test
    public void testGetAllOrders() {
        FilterOrder filterOrder = new FilterOrder();
        filterOrder.setCustomerEmail("test@example.com");
        filterOrder.setCustomerNumber("1234567890");
        filterOrder.setProductId(1L);

        Order order = new Order();
        order.setId(1L);

        Page<Order> page = new PageImpl<>(List.of(order));
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAllByFilter("test@example.com", "1234567890", 1L, pageable)).thenReturn(page);

        Page<Order> orders = orderService.getAllOrders(0, 10, filterOrder);

        assertEquals(1, orders.getTotalElements());
        assertEquals(order, orders.getContent().get(0));
    }
}
