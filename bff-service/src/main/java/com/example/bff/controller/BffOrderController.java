package com.example.bff.controller;

import com.example.bff.client.OrderClient;
import com.example.bff.model.OrderResponse;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/orders")
public class BffOrderController {

    private final OrderClient orderClient;

    public BffOrderController(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable("id") String id) {
        MDC.put("correlationId", java.util.UUID.randomUUID().toString());
        return orderClient.getOrder(id);
    }
}