package com.example.bff;

import com.example.bff.client.OrderClient;
import com.example.bff.model.OrderResponse;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.junit.jupiter.api.Test;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@SpringBootTest
class BffResilienceTest {

    @Autowired
    private OrderClient orderClient;

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @MockBean
    private RestTemplate restTemplate;

    @Test
    void testFallbackTriggered() {
        MDC.put("correlationId", "test123");

        // Simula falha persistente no serviço downstream
        when(restTemplate.getForObject(anyString(), eq(OrderResponse.class), anyString()))
                .thenThrow(new RuntimeException("Service down"));

        // Chamada real via proxy do Spring (Resilience4j entra em ação)
        OrderResponse response = orderClient.getOrder("1");

        // Valida que o fallback foi chamado
        assertEquals("UNKNOWN", response.status());

        // Opcional: verifica estado do circuit breaker
        var circuitBreaker = circuitBreakerRegistry.circuitBreaker("orderServiceCircuit");
        System.out.println("CircuitBreaker state: " + circuitBreaker.getState());
    }
}