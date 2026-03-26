package com.example.bff.client;

import com.example.bff.model.OrderResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.MDC;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

class OrderClientTest {

    private RestTemplate restTemplate;
    private OrderClient orderClient;

    @BeforeEach
    void setup() {
        restTemplate = Mockito.mock(RestTemplate.class);
        orderClient = new OrderClient(restTemplate);
        MDC.put("correlationId", "test-corr-id");
    }

    @Test
    void testGetOrder_success() {
        // Corrige a ambiguidade forçando Object[] para URI variables
        when(restTemplate.getForObject(anyString(), eq(OrderResponse.class), any(Object[].class)))
                .thenReturn(new OrderResponse("1", "CREATED"));

        OrderResponse response = orderClient.getOrder("1");
        assertEquals("CREATED", response.status());
    }

    @Test
    void testGetOrder_fallback() {
        RuntimeException simulated = new RuntimeException("simulated failure");

        OrderResponse response = orderClient.fallbackGetOrder("1", simulated);
        assertEquals("UNKNOWN", response.status());
    }
}