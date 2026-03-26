package com.example.order.controller;

import com.example.order.model.OrderResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final Random random = new Random();

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable String id) throws InterruptedException {

        // simula latência
        Thread.sleep(random.nextInt(2000));

        // simula falha
        if (random.nextInt(10) < 2) {
            throw new RuntimeException("Random failure in order-service");
        }

        return new OrderResponse(id, "CREATED");
    }
}