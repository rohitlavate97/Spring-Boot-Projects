# Spring Cloud Microservices

## Table of Contents
- [Microservices Architecture](#microservices-architecture)
- [Service Registry (Eureka)](#service-registry-eureka)
- [API Gateway](#api-gateway)
- [Config Server](#config-server)
- [Feign Client](#feign-client)
- [Circuit Breaker](#circuit-breaker)
- [Distributed Tracing](#distributed-tracing)

---

## Microservices Architecture

### Monolithic vs Microservices

| Feature | Monolithic | Microservices |
|---------|-----------|---------------|
| **Structure** | Single application | Multiple independent services |
| **Deployment** | Deploy entire application | Deploy services independently |
| **Scalability** | Scale entire application | Scale individual services |
| **Technology** | Single tech stack | Polyglot architecture |
| **Development** | Centralized team | Distributed teams |
| **Failure Impact** | Entire system down | Isolated failures |
| **Complexity** | Low initial complexity | Higher operational complexity |

### Microservices Components

```
┌─────────────────────────────────────────────────────┐
│                    API Gateway                       │
│            (Entry point for all requests)            │
└───────────────────┬─────────────────────────────────┘
                    │
        ┌───────────┼───────────┐
        │           │           │
        ▼           ▼           ▼
   ┌────────┐  ┌────────┐  ┌────────┐
   │Service │  │Service │  │Service │
   │   A    │  │   B    │  │   C    │
   └───┬────┘  └───┬────┘  └───┬────┘
       │           │           │
       └───────────┼───────────┘
                   │
                   ▼
        ┌──────────────────┐
        │  Service Registry │
        │     (Eureka)      │
        └──────────────────┘
                   │
                   ▼
        ┌──────────────────┐
        │  Config Server    │
        │ (Centralized)     │
        └──────────────────┘
```

### Key Microservices Patterns

1. **Service Discovery**: Eureka Server/Client
2. **API Gateway**: Single entry point
3. **Config Management**: Centralized configuration
4. **Load Balancing**: Distribute requests
5. **Circuit Breaker**: Handle failures gracefully
6. **Distributed Tracing**: Track requests across services

---

## Service Registry (Eureka)

### What is Service Registry?

Service Registry maintains a registry of available microservices and their instances.

**Benefits**:
- Dynamic service discovery
- Load balancing
- Health monitoring
- Automatic failover

### Eureka Server Setup

#### Dependencies (pom.xml)

```xml
<properties>
    <spring-cloud.version>2022.0.0</spring-cloud.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
    </dependency>
</dependencies>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

#### Application Class

```java
@SpringBootApplication
@EnableEurekaServer
public class ServiceRegistryApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceRegistryApplication.class, args);
    }
}
```

#### Configuration (application.yml)

```yaml
server:
  port: 8761

spring:
  application:
    name: SERVICE-REGISTRY

eureka:
  client:
    register-with-eureka: false
    fetch-registry: false
```

**Access**: `http://localhost:8761`

### Eureka Client (Microservice)

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### Application Class

```java
@SpringBootApplication
@EnableDiscoveryClient
public class GreetServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(GreetServiceApplication.class, args);
    }
}
```

#### Configuration (application.yml)

```yaml
server:
  port: 8081

spring:
  application:
    name: GREET-SERVICE

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

#### REST Controller

```java
@RestController
public class GreetRestController {
    
    @GetMapping("/greet")
    public String greet() {
        return "Good Morning!!";
    }
}
```

### Multiple Service Instances

Run same service on different ports for load balancing:

```bash
# Instance 1
java -jar greet-service.jar --server.port=8081

# Instance 2
java -jar greet-service.jar --server.port=8082

# Instance 3
java -jar greet-service.jar --server.port=8083
```

All instances register with Eureka automatically.

---

## API Gateway

### What is API Gateway?

API Gateway acts as a single entry point for all client requests.

**Responsibilities**:
- Request routing
- Load balancing
- Authentication/Authorization
- Rate limiting
- Request/Response transformation

### Flow

```
Client → API Gateway → Service Discovery → Target Microservice
```

### Gateway Setup

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### Application Class

```java
@SpringBootApplication
@EnableDiscoveryClient
public class ApiGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }
}
```

#### Configuration (application.yml)

```yaml
server:
  port: 3333

spring:
  application:
    name: API-GATEWAY
  cloud:
    gateway:
      routes:
        - id: greet-service
          uri: lb://GREET-SERVICE
          predicates:
            - Path=/greet/**
        
        - id: welcome-service
          uri: lb://WELCOME-SERVICE
          predicates:
            - Path=/welcome/**

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

**Access**:
- `http://localhost:3333/greet` → Routes to GREET-SERVICE
- `http://localhost:3333/welcome` → Routes to WELCOME-SERVICE

### Gateway Features

#### Load Balancing

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: user-service
          uri: lb://USER-SERVICE  # lb = Load Balanced
          predicates:
            - Path=/users/**
```

Gateway automatically distributes requests across multiple instances.

#### Filters

```yaml
spring:
  cloud:
    gateway:
      routes:
        - id: greet-service
          uri: lb://GREET-SERVICE
          predicates:
            - Path=/greet/**
          filters:
            - AddRequestHeader=X-Request-ID, 12345
            - AddResponseHeader=X-Response-Time, 100ms
            - RewritePath=/greet/(?<segment>.*), /${segment}
```

#### Custom Filter

```java
@Component
public class LoggingFilter implements GlobalFilter {
    
    private static final Logger logger = LoggerFactory.getLogger(LoggingFilter.class);
    
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        logger.info("Request Path: " + exchange.getRequest().getPath());
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            logger.info("Response Status Code: " + exchange.getResponse().getStatusCode());
        }));
    }
}
```

---

## Config Server

### What is Config Server?

Centralized configuration management for all microservices.

**Benefits**:
- Single source of truth
- Environment-specific configs
- Dynamic configuration updates
- Version control integration (Git)

### Config Server Setup

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### Application Class

```java
@SpringBootApplication
@EnableConfigServer
@EnableDiscoveryClient
public class ConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApplication.class, args);
    }
}
```

#### Configuration (application.yml)

```yaml
server:
  port: 8888

spring:
  application:
    name: CONFIG-SERVER
  cloud:
    config:
      server:
        git:
          uri: https://github.com/username/config-repo
          clone-on-start: true
          default-label: main

eureka:
  client:
    service-url:
      defaultZone: http://localhost:8761/eureka/
```

### Git Repository Structure

```
config-repo/
├── application.yml           # Common config
├── application-dev.yml       # Development config
├── application-prod.yml      # Production config
├── greet-service.yml         # Service-specific config
└── welcome-service.yml
```

**Example: greet-service.yml**
```yaml
msg: Good Morning from Config Server!

spring:
  datasource:
    url: jdbc:mysql://localhost:3306/greetdb
    username: root
    password: root123
```

### Config Client (Microservice)

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-config</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>
```

#### Bootstrap Configuration (bootstrap.yml)

```yaml
spring:
  application:
    name: greet-service
  cloud:
    config:
      uri: http://localhost:8888
      profile: dev
```

#### Using Configuration

```java
@RestController
@RefreshScope  // Enables dynamic config refresh
public class GreetRestController {
    
    @Value("${msg}")
    private String message;
    
    @GetMapping("/greet")
    public String greet() {
        return message;
    }
}
```

### Refresh Configuration

Update config in Git, then trigger refresh:

```bash
POST http://localhost:8081/actuator/refresh
```

Add dependency for refresh:
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

---

## Feign Client

### What is Feign Client?

Declarative REST client for inter-service communication.

**Benefits**:
- Simplified HTTP calls
- Load balancing integration
- Error handling
- Less boilerplate code

### Feign Client Setup

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-client</artifactId>
</dependency>
```

#### Enable Feign Clients

```java
@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
public class WelcomeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WelcomeServiceApplication.class, args);
    }
}
```

#### Feign Client Interface

```java
@FeignClient(name = "GREET-SERVICE")
public interface GreetFeignClient {
    
    @GetMapping("/greet")
    public String invokeGreetMsg();
}
```

#### Using Feign Client

```java
@RestController
public class WelcomeRestController {
    
    @Autowired
    private GreetFeignClient greetClient;
    
    @GetMapping("/welcome")
    public String getWelcomeMsg() {
        String greetMsg = greetClient.invokeGreetMsg();
        String welcomeMsg = "Welcome to our application";
        return greetMsg + ", " + welcomeMsg;
    }
}
```

### Feign Configuration

#### Timeouts

```yaml
feign:
  client:
    config:
      default:
        connect-timeout: 5000
        read-timeout: 10000
```

#### Logging

```yaml
logging:
  level:
    com.example.client.GreetFeignClient: DEBUG
```

```java
@Configuration
public class FeignConfig {
    
    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
}
```

---

## Circuit Breaker

### What is Circuit Breaker?

Circuit Breaker prevents cascading failures when a service is down.

**States**:
1. **CLOSED**: Requests flow normally
2. **OPEN**: Requests fail immediately (service down)
3. **HALF_OPEN**: Test if service recovered

### Circuit Breaker Flow

```
Request → Circuit Breaker → Service
                ↓
           If service fails repeatedly
                ↓
           Circuit OPENS
                ↓
           Fallback method executed
```

### Resilience4j Setup

#### Dependencies

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

#### Configuration (application.yml)

```yaml
resilience4j:
  circuitbreaker:
    instances:
      greetService:
        register-health-indicator: true
        sliding-window-size: 10
        minimum-number-of-calls: 5
        permitted-number-of-calls-in-half-open-state: 3
        wait-duration-in-open-state: 10s
        failure-rate-threshold: 50
        automatic-transition-from-open-to-half-open-enabled: true

management:
  endpoints:
    web:
      exposure:
        include: '*'
  health:
    circuitbreakers:
      enabled: true
```

#### Using Circuit Breaker

```java
@RestController
public class WelcomeRestController {
    
    @Autowired
    private GreetFeignClient greetClient;
    
    @GetMapping("/welcome")
    @CircuitBreaker(name = "greetService", fallbackMethod = "fallbackGreet")
    public String getWelcomeMsg() {
        String greetMsg = greetClient.invokeGreetMsg();
        return greetMsg + ", Welcome!";
    }
    
    // Fallback method - same signature + Throwable
    public String fallbackGreet(Throwable throwable) {
        return "Greet service is down! Please try after some time.";
    }
}
```

### Retry Pattern

```yaml
resilience4j:
  retry:
    instances:
      greetService:
        max-attempts: 3
        wait-duration: 2s
```

```java
@GetMapping("/welcome")
@Retry(name = "greetService", fallbackMethod = "fallbackGreet")
@CircuitBreaker(name = "greetService", fallbackMethod = "fallbackGreet")
public String getWelcomeMsg() {
    String greetMsg = greetClient.invokeGreetMsg();
    return greetMsg + ", Welcome!";
}
```

### Rate Limiter

```yaml
resilience4j:
  ratelimiter:
    instances:
      greetService:
        limit-for-period: 10
        limit-refresh-period: 1s
        timeout-duration: 0
```

```java
@GetMapping("/welcome")
@RateLimiter(name = "greetService", fallbackMethod = "fallbackGreet")
public String getWelcomeMsg() {
    return greetClient.invokeGreetMsg();
}
```

---

## Distributed Tracing

### What is Distributed Tracing?

Tracks requests as they flow through multiple microservices.

**Benefits**:
- Performance monitoring
- Troubleshooting
- Request flow visualization
- Latency analysis

### Zipkin Setup

#### Run Zipkin Server

```bash
# Download and run Zipkin
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar
```

**Access**: `http://localhost:9411`

#### Microservice Dependencies

```xml
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-tracing-bridge-brave</artifactId>
</dependency>

<dependency>
    <groupId>io.zipkin.reporter2</groupId>
    <artifactId>zipkin-reporter-brave</artifactId>
</dependency>
```

#### Configuration

```yaml
spring:
  application:
    name: greet-service
  zipkin:
    base-url: http://localhost:9411/

management:
  tracing:
    sampling:
      probability: 1.0  # 100% sampling for development
```

### Viewing Traces

1. Make requests to your microservices
2. Open Zipkin UI: `http://localhost:9411`
3. Click "Run Query" to see traces
4. Click on a trace to see detailed flow

### Trace Example

```
WELCOME-SERVICE (50ms)
  └─> GREET-SERVICE (30ms)
      └─> DATABASE (20ms)
```

---

## Microservices Best Practices

### 1. Design Principles

- **Single Responsibility**: Each service has one business capability
- **Loosely Coupled**: Services are independent
- **High Cohesion**: Related functionality together
- **API First**: Design APIs before implementation

### 2. Communication

- **Synchronous**: REST, gRPC for request/response
- **Asynchronous**: Message queues for event-driven
- **Service Discovery**: Use Eureka for dynamic discovery

### 3. Data Management

- **Database per Service**: Each service owns its data
- **Saga Pattern**: Distributed transactions
- **Event Sourcing**: Store events, not just state

### 4. Security

- **API Gateway Security**: Centralized authentication
- **Service-to-Service Auth**: JWT tokens, OAuth2
- **Secret Management**: Vault, AWS Secrets Manager

### 5. Monitoring & Logging

- **Centralized Logging**: ELK Stack (Elasticsearch, Logstash, Kibana)
- **Distributed Tracing**: Zipkin, Jaeger
- **Metrics**: Prometheus, Grafana
- **Health Checks**: Actuator endpoints

### 6. Deployment

- **Containerization**: Docker
- **Orchestration**: Kubernetes
- **CI/CD**: Jenkins, GitLab CI
- **Blue-Green Deployment**: Zero downtime

---

*Continue to [07-Spring-Security.md](07-Spring-Security.md) for security implementation.*
