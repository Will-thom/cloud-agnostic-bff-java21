package com.example.bff.controller;

import com.example.bff.client.PaymentClient;
import com.example.bff.model.PaymentResponse;
import org.slf4j.MDC;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class BffPaymentController {

    private final PaymentClient paymentClient;

    public BffPaymentController(PaymentClient paymentClient) {
        this.paymentClient = paymentClient;
    }

    @GetMapping("/{id}")
    public PaymentResponse getPayment(@PathVariable("id") String id) {
        MDC.put("correlationId", java.util.UUID.randomUUID().toString());
        return paymentClient.getPayment(id);
    }
}