package com.example.bff;

import com.example.bff.client.OrderClient;
import com.example.bff.model.OrderResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class BffService {

    private static final Logger log = LoggerFactory.getLogger(BffService.class);

    private final OrderClient orderClient;

    public BffService(OrderClient orderClient) {
        this.orderClient = orderClient;
    }

    @Retry(name = "orderClientRetry", fallbackMethod = "fallbackOrder")
    @CircuitBreaker(name = "orderClientCircuit", fallbackMethod = "fallbackOrder")
    public OrderResponse getOrder(String orderId) {
        // adiciona correlationId nos logs
        String correlationId = UUID.randomUUID().toString();
        MDC.put("correlationId", correlationId);

        log.info("Chamando order-service para orderId={}", orderId);

        OrderResponse response = orderClient.getOrder(orderId);

        log.info("Resposta recebida orderId={}, status={}", response.orderId(), response.status());

        MDC.remove("correlationId");
        return response;
    }

    // Fallback quando falha ou circuit breaker abre
    public OrderResponse fallbackOrder(String orderId, Throwable t) {
        String correlationId = MDC.get("correlationId");
        log.error("Fallback acionado para orderId={} correlationId={} motivo={}",
                orderId, correlationId, t.getMessage());

        return new OrderResponse(orderId, "UNKNOWN");
    }
}