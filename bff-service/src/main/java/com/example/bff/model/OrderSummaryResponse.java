package com.example.bff.model;

public record OrderSummaryResponse(
        OrderResponse order,
        PaymentResponse payment,
        DeliveryResponse delivery
) {}