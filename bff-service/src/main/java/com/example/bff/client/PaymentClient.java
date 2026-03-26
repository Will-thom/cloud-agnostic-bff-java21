package com.example.bff.client;

import com.example.bff.model.PaymentResponse;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class PaymentClient {

    private static final Logger log = LoggerFactory.getLogger(PaymentClient.class);
    private final RestTemplate restTemplate;

    public PaymentClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Retry(name = "paymentServiceRetry")
    @CircuitBreaker(name = "paymentServiceCircuit", fallbackMethod = "fallbackGetPayment")
    public PaymentResponse getPayment(String id) {
        String correlationId = MDC.get("correlationId");
        log.info("Fetching payment {} with correlationId {}", id, correlationId);

        return restTemplate.getForObject(
                "http://payment-service:8082/payments/{id}",
                PaymentResponse.class,
                id
        );
    }

    // Fallback chamado quando o circuit breaker abrir ou houver erro persistente
    public PaymentResponse fallbackGetPayment(String id, Throwable ex) {
        String correlationId = MDC.get("correlationId");
        log.warn("Fallback called for payment {} with correlationId {} due to {}", id, correlationId, ex.toString());

        // Corrigido: status deve ser String
        return new PaymentResponse(id, "UNKNOWN");
    }
}