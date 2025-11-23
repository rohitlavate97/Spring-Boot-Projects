# Messaging and Integration

## Table of Contents
- [Apache Kafka](#apache-kafka)
- [Redis Cache](#redis-cache)
- [Event-Driven Architecture](#event-driven-architecture)

---

## Apache Kafka

### What is Kafka?

**Apache Kafka** is a distributed event streaming platform for high-throughput, fault-tolerant messaging.

### Kafka Components

| Component | Description |
|-----------|-------------|
| **Producer** | Publishes messages to topics |
| **Consumer** | Subscribes to topics and processes messages |
| **Topic** | Category/feed name to which records are published |
| **Broker** | Kafka server that stores data |
| **Partition** | Topic is divided into partitions for scalability |
| **ZooKeeper** | Manages Kafka cluster metadata |

### Kafka Architecture

```
Producer → Topic (Partition 0, Partition 1, Partition 2) → Consumer Group
                                                              ├─ Consumer 1
                                                              ├─ Consumer 2
                                                              └─ Consumer 3
```

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
```

### Configuration (application.yml)

```yaml
spring:
  kafka:
    bootstrap-servers: localhost:9092
    
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
      
    consumer:
      group-id: my-consumer-group
      key-deserializer: org.apache.kafka.common.serialization.StringDeserializer
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: '*'
      auto-offset-reset: earliest
```

### Kafka Producer

#### Message Entity

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    private String orderId;
    private String productName;
    private Double price;
    private Integer quantity;
    private LocalDateTime orderDate;
}
```

#### Producer Service

```java
@Service
public class KafkaProducerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    
    @Autowired
    private KafkaTemplate<String, Order> kafkaTemplate;
    
    private static final String TOPIC = "order-topic";
    
    public void sendOrder(Order order) {
        logger.info("Sending order: {}", order);
        
        kafkaTemplate.send(TOPIC, order.getOrderId(), order)
            .whenComplete((result, ex) -> {
                if (ex == null) {
                    logger.info("Order sent successfully: {}", result.getRecordMetadata());
                } else {
                    logger.error("Failed to send order: {}", ex.getMessage());
                }
            });
    }
}
```

#### Producer Controller

```java
@RestController
@RequestMapping("/api/orders")
public class OrderController {
    
    @Autowired
    private KafkaProducerService producerService;
    
    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody Order order) {
        order.setOrderDate(LocalDateTime.now());
        producerService.sendOrder(order);
        return ResponseEntity.ok("Order sent to Kafka successfully");
    }
}
```

### Kafka Consumer

#### Consumer Service

```java
@Service
public class KafkaConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);
    
    @KafkaListener(topics = "order-topic", groupId = "my-consumer-group")
    public void consumeOrder(Order order) {
        logger.info("Received order: {}", order);
        
        // Process the order
        processOrder(order);
    }
    
    private void processOrder(Order order) {
        // Business logic - save to database, send notification, etc.
        logger.info("Processing order: {} for product: {}", 
                    order.getOrderId(), order.getProductName());
    }
}
```

### Multiple Consumers

```java
@Service
public class KafkaMultiConsumerService {
    
    private static final Logger logger = LoggerFactory.getLogger(KafkaMultiConsumerService.class);
    
    // Email notification consumer
    @KafkaListener(topics = "order-topic", groupId = "email-group")
    public void sendEmailNotification(Order order) {
        logger.info("Sending email notification for order: {}", order.getOrderId());
        // Send email logic
    }
    
    // Inventory update consumer
    @KafkaListener(topics = "order-topic", groupId = "inventory-group")
    public void updateInventory(Order order) {
        logger.info("Updating inventory for product: {}", order.getProductName());
        // Update inventory logic
    }
    
    // Analytics consumer
    @KafkaListener(topics = "order-topic", groupId = "analytics-group")
    public void recordAnalytics(Order order) {
        logger.info("Recording analytics for order: {}", order.getOrderId());
        // Analytics logic
    }
}
```

### Kafka Topic Configuration

```java
@Configuration
public class KafkaTopicConfig {
    
    @Bean
    public NewTopic orderTopic() {
        return TopicBuilder.name("order-topic")
                .partitions(3)
                .replicas(1)
                .build();
    }
    
    @Bean
    public NewTopic notificationTopic() {
        return TopicBuilder.name("notification-topic")
                .partitions(2)
                .replicas(1)
                .compact()
                .build();
    }
}
```

### Kafka Error Handling

```java
@Configuration
public class KafkaErrorHandlingConfig {
    
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Order> kafkaListenerContainerFactory(
            ConsumerFactory<String, Order> consumerFactory) {
        
        ConcurrentKafkaListenerContainerFactory<String, Order> factory = 
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        
        // Error handling
        factory.setCommonErrorHandler(new DefaultErrorHandler(
            new FixedBackOff(1000L, 3)  // Retry 3 times with 1 second delay
        ));
        
        return factory;
    }
}
```

### Testing Kafka

#### Start Kafka (using Docker)

```bash
docker-compose up -d
```

**docker-compose.yml**:
```yaml
version: '3'
services:
  zookeeper:
    image: confluentinc/cp-zookeeper:latest
    environment:
      ZOOKEEPER_CLIENT_PORT: 2181
      ZOOKEEPER_TICK_TIME: 2000
    ports:
      - "2181:2181"
  
  kafka:
    image: confluentinc/cp-kafka:latest
    depends_on:
      - zookeeper
    ports:
      - "9092:9092"
    environment:
      KAFKA_BROKER_ID: 1
      KAFKA_ZOOKEEPER_CONNECT: zookeeper:2181
      KAFKA_ADVERTISED_LISTENERS: PLAINTEXT://localhost:9092
      KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR: 1
```

#### Send Test Message

```bash
POST http://localhost:8080/api/orders
Content-Type: application/json

{
    "orderId": "ORD-001",
    "productName": "Laptop",
    "price": 1200.00,
    "quantity": 2
}
```

---

## Redis Cache

### What is Redis?

**Redis** is an in-memory data structure store used as:
- Cache
- Message broker
- Database

### Benefits

- Ultra-fast (in-memory)
- Supports various data structures
- Persistence options
- Pub/Sub messaging
- Distributed caching

### Dependencies

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-cache</artifactId>
</dependency>
```

### Configuration (application.yml)

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password: # if required
    timeout: 2000
    
  cache:
    type: redis
    redis:
      time-to-live: 600000  # 10 minutes
      cache-null-values: false
```

### Redis Configuration Class

```java
@Configuration
@EnableCaching
public class RedisConfig {
    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // JSON serialization
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper mapper = new ObjectMapper();
        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        mapper.activateDefaultTyping(
            mapper.getPolymorphicTypeValidator(),
            ObjectMapper.DefaultTyping.NON_FINAL
        );
        serializer.setObjectMapper(mapper);
        
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);
        
        template.afterPropertiesSet();
        return template;
    }
    
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();
        
        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .build();
    }
}
```

### Using Redis Cache (Annotation-Based)

#### Entity

```java
@Data
@Entity
public class Product implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private Double price;
    private String category;
}
```

#### Service with Caching

```java
@Service
public class ProductService {
    
    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    
    @Autowired
    private ProductRepository repository;
    
    @Cacheable(value = "products", key = "#id")
    public Product getProductById(Long id) {
        logger.info("Fetching product from database: {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }
    
    @Cacheable(value = "products")
    public List<Product> getAllProducts() {
        logger.info("Fetching all products from database");
        return repository.findAll();
    }
    
    @CachePut(value = "products", key = "#product.id")
    public Product updateProduct(Product product) {
        logger.info("Updating product in database: {}", product.getId());
        return repository.save(product);
    }
    
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(Long id) {
        logger.info("Deleting product from database: {}", id);
        repository.deleteById(id);
    }
    
    @CacheEvict(value = "products", allEntries = true)
    public void clearAllCache() {
        logger.info("Clearing all product cache");
    }
}
```

### Cache Annotations

| Annotation | Purpose | When to Use |
|-----------|---------|-------------|
| `@Cacheable` | Caches method result | Read operations |
| `@CachePut` | Updates cache | Update operations |
| `@CacheEvict` | Removes from cache | Delete operations |
| `@Caching` | Multiple cache operations | Complex scenarios |

### Using RedisTemplate (Programmatic)

```java
@Service
public class RedisService {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    // String operations
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }
    
    public String getValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }
    
    public void setValueWithExpiry(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }
    
    // Hash operations
    public void setHashValue(String key, String hashKey, Object value) {
        redisTemplate.opsForHash().put(key, hashKey, value);
    }
    
    public Object getHashValue(String key, String hashKey) {
        return redisTemplate.opsForHash().get(key, hashKey);
    }
    
    // List operations
    public void addToList(String key, Object value) {
        redisTemplate.opsForList().rightPush(key, value);
    }
    
    public List<Object> getList(String key) {
        return redisTemplate.opsForList().range(key, 0, -1);
    }
    
    // Set operations
    public void addToSet(String key, Object value) {
        redisTemplate.opsForSet().add(key, value);
    }
    
    public Set<Object> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }
    
    // Delete operations
    public void delete(String key) {
        redisTemplate.delete(key);
    }
    
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
    
    // Expiration
    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }
}
```

### Redis Session Management

```xml
<dependency>
    <groupId>org.springframework.session</groupId>
    <artifactId>spring-session-data-redis</artifactId>
</dependency>
```

```java
@Configuration
@EnableRedisHttpSession
public class RedisSessionConfig {
    // Automatic session management in Redis
}
```

### Testing Redis

#### Start Redis (Docker)

```bash
docker run -d -p 6379:6379 --name redis redis:latest
```

#### Test Cache

```bash
# First call - hits database
GET http://localhost:8080/api/products/1
# Response time: 200ms (from DB)

# Second call - hits cache
GET http://localhost:8080/api/products/1
# Response time: 5ms (from Redis)
```

---

## Event-Driven Architecture

### What is Event-Driven Architecture?

Event-Driven Architecture (EDA) is a design pattern where services communicate through events.

### Benefits

- **Loose Coupling**: Services are independent
- **Scalability**: Easy to add new consumers
- **Resilience**: Failures isolated
- **Real-time**: Immediate event processing

### Event Flow

```
Order Service → [Order Created Event] → Kafka Topic
                                            ↓
                          ┌─────────────────┼─────────────────┐
                          ↓                 ↓                 ↓
                  Email Service     Inventory Service   Analytics Service
```

### Event Entity

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderEvent {
    private String eventId;
    private String eventType;  // ORDER_CREATED, ORDER_UPDATED, ORDER_CANCELLED
    private LocalDateTime timestamp;
    private Order order;
}
```

### Event Publisher

```java
@Service
public class OrderEventPublisher {
    
    private static final Logger logger = LoggerFactory.getLogger(OrderEventPublisher.class);
    
    @Autowired
    private KafkaTemplate<String, OrderEvent> kafkaTemplate;
    
    private static final String TOPIC = "order-events";
    
    public void publishOrderCreatedEvent(Order order) {
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            "ORDER_CREATED",
            LocalDateTime.now(),
            order
        );
        
        logger.info("Publishing ORDER_CREATED event: {}", event.getEventId());
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
    }
    
    public void publishOrderUpdatedEvent(Order order) {
        OrderEvent event = new OrderEvent(
            UUID.randomUUID().toString(),
            "ORDER_UPDATED",
            LocalDateTime.now(),
            order
        );
        
        logger.info("Publishing ORDER_UPDATED event: {}", event.getEventId());
        kafkaTemplate.send(TOPIC, event.getEventId(), event);
    }
}
```

### Event Consumers

```java
@Service
public class EmailEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailEventConsumer.class);
    
    @KafkaListener(topics = "order-events", groupId = "email-service")
    public void handleOrderEvent(OrderEvent event) {
        switch (event.getEventType()) {
            case "ORDER_CREATED":
                sendOrderConfirmationEmail(event.getOrder());
                break;
            case "ORDER_UPDATED":
                sendOrderUpdateEmail(event.getOrder());
                break;
            default:
                logger.warn("Unknown event type: {}", event.getEventType());
        }
    }
    
    private void sendOrderConfirmationEmail(Order order) {
        logger.info("Sending confirmation email for order: {}", order.getOrderId());
        // Email sending logic
    }
    
    private void sendOrderUpdateEmail(Order order) {
        logger.info("Sending update email for order: {}", order.getOrderId());
    }
}

@Service
public class InventoryEventConsumer {
    
    private static final Logger logger = LoggerFactory.getLogger(InventoryEventConsumer.class);
    
    @KafkaListener(topics = "order-events", groupId = "inventory-service")
    public void handleOrderEvent(OrderEvent event) {
        if ("ORDER_CREATED".equals(event.getEventType())) {
            updateInventory(event.getOrder());
        }
    }
    
    private void updateInventory(Order order) {
        logger.info("Updating inventory for product: {}", order.getProductName());
        // Inventory update logic
    }
}
```

### Saga Pattern (Distributed Transactions)

```java
@Service
public class OrderSagaOrchestrator {
    
    @Autowired
    private OrderEventPublisher eventPublisher;
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Transactional
    public void createOrder(Order order) {
        // Step 1: Save order
        Order savedOrder = orderRepository.save(order);
        
        // Step 2: Publish event
        eventPublisher.publishOrderCreatedEvent(savedOrder);
    }
    
    @KafkaListener(topics = "inventory-response", groupId = "order-saga")
    public void handleInventoryResponse(InventoryResponse response) {
        if (!response.isAvailable()) {
            // Compensating transaction
            cancelOrder(response.getOrderId());
        }
    }
    
    private void cancelOrder(String orderId) {
        // Rollback logic
    }
}
```

---

*Continue to [09-Batch-Scheduling.md](09-Batch-Scheduling.md) for batch processing and scheduling.*
