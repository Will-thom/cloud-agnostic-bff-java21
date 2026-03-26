package com.example.bff.client;

import com.example.bff.model.DeliveryResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class DeliveryClient {

    private static final Logger log = LoggerFactory.getLogger(DeliveryClient.class);
    private final RestTemplate restTemplate;

    public DeliveryClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "deliveryServiceRetry")
    @CircuitBreaker(name = "deliveryServiceCircuit", fallbackMethod = "fallbackGetDelivery")
    public DeliveryResponse getDelivery(String id) {
        String correlationId = MDC.get("correlationId");
        log.info("Fetching delivery {} with correlationId {}", id, correlationId);

        return restTemplate.getForObject(
                "http://delivery-service:8083/deliveries/{id}",
                DeliveryResponse.class,
                id
        );
    }

    // Fallback chamado quando o circuit breaker está aberto ou há erro persistente
    public DeliveryResponse fallbackGetDelivery(String id, Throwable ex) {
        String correlationId = MDC.get("correlationId");
        log.warn("Fallback called for delivery {} with correlationId {} due to {}", id, correlationId, ex.toString());

        // Retorna um status padrão indicando que não há informação
        return new DeliveryResponse(id, "UNKNOWN");
    }
}