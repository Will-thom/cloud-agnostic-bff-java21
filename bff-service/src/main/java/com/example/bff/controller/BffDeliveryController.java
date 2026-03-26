package com.example.bff.controller;

import com.example.bff.client.DeliveryClient;
import com.example.bff.model.DeliveryResponse;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deliveries")
public class BffDeliveryController {

    private final DeliveryClient deliveryClient;

    public BffDeliveryController(DeliveryClient deliveryClient) {
        this.deliveryClient = deliveryClient;
    }

    @GetMapping("/{id}")
    public DeliveryResponse getDelivery(@PathVariable("id") String id) {
        MDC.put("correlationId", java.util.UUID.randomUUID().toString());
        return deliveryClient.getDelivery(id);
    }
}