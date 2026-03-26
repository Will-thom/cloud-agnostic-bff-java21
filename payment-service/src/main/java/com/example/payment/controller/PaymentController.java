package com.example.payment.controller;

import com.example.payment.model.PaymentResponse;
import org.springframework.web.bind.annotation.*;

import java.util.Random;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final Random random = new Random();

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable String id) throws InterruptedException {

        // simula latência
        Thread.sleep(random.nextInt(2000));

        // simula falha
        if (random.nextInt(10) < 2) {
            throw new RuntimeException("Random failure in payment-service");
        }

        return new PaymentResponse(id, "PAID");
    }
}