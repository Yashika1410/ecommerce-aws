package com.example.ecommerce.controller;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.example.ecommerce.dtos.requests.FilterOrder;
import com.example.ecommerce.dtos.requests.OrderRequest;
import com.example.ecommerce.dtos.responses.ResponseWrapper;
import com.example.ecommerce.entities.Order;
import com.example.ecommerce.services.OrderService;
import com.example.ecommerce.utils.ParserUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;




@SpringBootTest
@Slf4j
public class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;


    private OrderController orderController;

    @BeforeEach
    public void setup() {
        orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    public void testPlaceOrder() throws Exception {
        OrderRequest orderRequest = OrderRequest.builder().productId(Long.valueOf(1)).customerEmail("sample@gmail.com").customerNumber("1234567890").build();
        Order order =  Order.builder().customerEmail("sample@gmail.com").customerNumber("1234567890").build();
        ResponseWrapper responseWrapper = ResponseWrapper.convertObjectToResponseMapperForSuccess(order, "Order placed successfully");

        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(order);
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParserUtils.writeValueAsString(orderRequest)))
                .andExpect(content().string(ParserUtils.writeValueAsString(responseWrapper)))
                .andExpect(status().isOk());
    }
    @Test
    public void testNegativePlaceOrder() throws Exception {
        OrderRequest orderRequest = OrderRequest.builder().customerEmail("sample@gmail.com").customerNumber("1234567890").build();;
        ResponseWrapper responseWrapper = ResponseWrapper.builder().success(false).message("Product Id is required").code(400).build();

        when(orderService.placeOrder(any(OrderRequest.class))).thenReturn(Order.builder().customerEmail("sample@gmail.com").build());
        mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParserUtils.writeValueAsString(orderRequest)))
                .andExpect(content().string(ParserUtils.writeValueAsString(responseWrapper)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetOrderDetailsByFilter() throws Exception {
        FilterOrder filterOrder = new FilterOrder();

        when(orderService.getAllOrders(anyInt(), anyInt(), any(FilterOrder.class))).thenReturn(Page.empty());

        mockMvc.perform(post("/api/v1/orders/orders/filter")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ParserUtils.writeValueAsString(filterOrder))
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk());
    }
}