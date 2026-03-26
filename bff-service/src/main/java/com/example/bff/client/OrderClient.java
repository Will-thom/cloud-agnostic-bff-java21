package com.example.bff.client;

import com.example.bff.model.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class OrderClient {

    private static final Logger log = LoggerFactory.getLogger(OrderClient.class);
    private final RestTemplate restTemplate;

    public OrderClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "orderServiceRetry")
    @CircuitBreaker(name = "orderServiceCircuit", fallbackMethod = "fallbackGetOrder")
    public OrderResponse getOrder(String id) {
        String correlationId = MDC.get("correlationId");
        log.info("Fetching order {} with correlationId {}", id, correlationId);

        return restTemplate.getForObject("http://order-service:8081/orders/{id}", OrderResponse.class, id);
    }

    public OrderResponse fallbackGetOrder(String id, Throwable ex) {
        String correlationId = MDC.get("correlationId");
        log.warn("Fallback called for order {} with correlationId {} due to {}", id, correlationId, ex.toString());
        return new OrderResponse(id, "UNKNOWN");
    }
}