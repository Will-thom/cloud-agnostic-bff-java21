package com.example.delivery.controller;

import com.example.delivery.model.DeliveryResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final Random random = new Random();

    @GetMapping("/{id}")
    public DeliveryResponse getDelivery(@PathVariable String id) throws InterruptedException {

        // simula latência aleatória
        Thread.sleep(random.nextInt(2000));

        // simula falha aleatória (20% de chance)
        if (random.nextInt(10) < 2) {
            throw new RuntimeException("Random failure in delivery-service");
        }

        // resposta padrão
        return new DeliveryResponse(id, "SCHEDULED");
    }
}