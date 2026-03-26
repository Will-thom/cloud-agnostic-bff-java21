# BFF Orchestrator (Java 21 + Spring Boot + Resilience4j + Docker)

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![Docker](https://img.shields.io/badge/docker-ready-blue)
![Java](https://img.shields.io/badge/java-21-orange)

Implantação de **BFF moderno, resiliente e cloud-agnostic**, usando Docker, Resilience4j, logs estruturados e observabilidade planejada (OpenTelemetry, Prometheus, Grafana, Jaeger).

---

## 🎯 Objetivo

Construir um **Backend For Frontend (BFF)** que simula produção real:

- microservices desacoplados
- resiliência (retry, fallback, circuit breaker)
- logging estruturado (correlationId)
- comunicação via rede Docker interna
- totalmente **cloud-agnostic** (substitui AWS API Gateway + Lambda + X-Ray)

---

## 🧱 Stack

- **Java 21 + Spring Boot 3.x**
- **Spring Web (MVC)** + RestTemplate
- **Resilience4j**: retry, circuit breaker, fallback
- **Docker + Docker Compose**
- **Logs estruturados** com MDC (correlationId)
- Observabilidade futura: OpenTelemetry, Jaeger, Prometheus, Grafana

---

## 📦 Estrutura do projeto

```text
bff-orchestrator-java21/
│
├── bff-service/
├── order-service/
├── payment-service/
├── delivery-service/
│
├── infra/
│   └── docker-compose.yml
│
└── README.md


🔹 Microservices implementados

Serviço	Porta interna	Endpoint(s)
order-service	8081	/orders/{id}
payment-service	8082	/payments/{id}
delivery-service	8083	/deliveries/{id}
bff-service	8080	/orders/{id}, /payments/{id}, /deliveries/{id}

🔹 Como rodar

1️⃣ Subir todos os serviços via Docker Compose

cd infra
docker-compose up --build

Isso cria uma rede interna onde cada container se comunica pelo nome do container.

2️⃣ Testar endpoints via host

curl http://localhost:8080/orders/1
curl http://localhost:8080/payments/1
curl http://localhost:8080/deliveries/1

3️⃣ Testar comunicação interna (dentro do container BFF)

docker exec -it bff-service sh
curl http://order-service:8081/orders/1
curl http://payment-service:8082/payments/1
curl http://delivery-service:8083/deliveries/1

🔹 Resiliência

Retry configurado (Resilience4j)
Circuit breaker configurado
Fallback retorna JSON:
{"status":"UNKNOWN"}

Teste de falha aleatória:

for i in {1..10}; do curl http://localhost:8080/orders/1; done

Monitorar circuit breaker:
curl http://localhost:8080/actuator/circuitbreakers

🔹 Logging estruturado

logging.pattern.console: "%d{yyyy-MM-dd HH:mm:ss.SSS} [%X{correlationId}] %-5level %logger{36} - %msg%n"
correlationId propagado entre serviços
Permite rastrear requisições distribuídas

🔹 Diagrama de comunicação
+----------------+       +-----------------+
|  bff-service   |------>| order-service   |
| (8080)         |------>| payment-service |
|                |------>| delivery-service|
+----------------+       +-----------------+

Todas chamadas internas via nome do container + porta interna
Docker Compose gerencia a rede


🚧 Próximos passos

Structured Concurrency no BFF
Usar StructuredTaskScope do Java 21
Paralelizar chamadas order + payment + delivery
Endpoint agregado: /order-details/{id}
Observabilidade real
OpenTelemetry tracing distribuído
Jaeger para traces
Prometheus + Grafana para métricas
Testes avançados
Testcontainers para dependências externas
WireMock para simular falhas específicas
Testes de resiliência reais
Infra completa
Healthchecks no docker-compose
Rede otimizada
Alertas e dashboards

💡 Nota final

Projeto simula um backend moderno resiliente, pronto para evoluir para produção real com:

microservices isolados
BFF centralizado
retry, fallback, circuit breaker
observabilidade futura

Tudo cloud-agnostic e pronto para Docker.
