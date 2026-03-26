package com.example.bff;

import com.example.bff.client.OrderClient;
import com.example.bff.model.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;

@Service
public class OrderOrchestratorService {

    private final OrderClient orderClient;

    public OrderOrchestratorService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @CircuitBreaker(name = "orderService", fallbackMethod = "fallbackOrder")
    @Retry(name = "orderService")
    public OrderResponse getOrder(String id) {
        return orderClient.getOrder(id);
    }

    public OrderResponse fallbackOrder(String id, Throwable t) {
        System.out.println("[Fallback] OrderService indisponível para orderId=" + id + " : " + t.getMessage());
        return new OrderResponse(id, "UNKNOWN");
    }
}