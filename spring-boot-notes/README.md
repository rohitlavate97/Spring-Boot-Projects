# Spring Boot Complete Notes

Comprehensive documentation covering all aspects of Spring Boot development, from fundamentals to advanced topics.

## 📚 Table of Contents

### Core Concepts
1. **[Spring Boot Overview](01-Spring-Boot-Overview.md)**
   - Microservices Architecture
   - Spring Framework Fundamentals
   - Inversion of Control (IoC)
   - Dependency Injection
   - Bean Lifecycle and Scopes

2. **[Spring Boot Fundamentals](02-Spring-Boot-Fundamentals.md)**
   - Application Runners
   - Spring Boot Actuator
   - Embedded Servers
   - Profiles and Configuration
   - YAML vs Properties

3. **[Spring Data JPA](03-Spring-Data-JPA.md)**
   - Repository Interfaces
   - CRUD Operations
   - Custom Queries (JPQL, Native SQL)
   - Pagination and Sorting
   - Primary Key Generators
   - Composite Keys and Timestamps

### Web Development
4. **[Spring Web MVC](04-Spring-Web-MVC.md)**
   - MVC Architecture
   - Controllers and Request Mapping
   - Form Handling and Binding
   - Thymeleaf vs JSP
   - Form Validation
   - Exception Handling

5. **[REST API Development](05-REST-API.md)**
   - REST Principles
   - HTTP Methods and Status Codes
   - Request/Response Handling
   - Content Negotiation (JSON/XML)
   - Swagger Documentation
   - REST Clients (WebClient, RestTemplate)

### Distributed Systems
6. **[Microservices Architecture](06-Microservices.md)**
   - Service Registry (Eureka)
   - API Gateway
   - Config Server
   - Feign Client
   - Circuit Breaker (Resilience4j)
   - Distributed Tracing (Zipkin)

7. **[Spring Security](07-Spring-Security.md)**
   - Authentication vs Authorization
   - Basic Authentication
   - Form-Based Login
   - JWT Authentication
   - OAuth2 Integration
   - Method-Level Security

### Integration & Messaging
8. **[Messaging and Integration](08-Messaging-Integration.md)**
   - Apache Kafka
     - Producers and Consumers
     - Topic Configuration
     - Error Handling
   - Redis Cache
     - Cache Annotations
     - RedisTemplate
     - Session Management
   - Event-Driven Architecture

9. **[Batch Processing and Scheduling](09-Batch-Scheduling.md)**
   - Spring Batch
     - Job Configuration
     - ItemReader, ItemProcessor, ItemWriter
     - Multi-Step Jobs
   - Task Scheduling
     - Fixed Rate/Delay
     - Cron Expressions
     - Async Execution

### Quality Assurance
10. **[Testing](10-Testing.md)**
    - Unit Testing (JUnit 5)
    - Integration Testing
    - REST API Testing
    - Mocking with Mockito
    - Test Coverage (JaCoCo)

---

## 🚀 Quick Start Guide

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- IDE (IntelliJ IDEA, Eclipse, VS Code)
- MySQL/PostgreSQL (for database applications)

### Create Your First Spring Boot Application

#### 1. Using Spring Initializr
Visit [start.spring.io](https://start.spring.io) and configure:
- Project: Maven
- Language: Java
- Spring Boot: 3.x.x
- Dependencies: Spring Web, Spring Data JPA, MySQL Driver, Lombok

#### 2. Project Structure
```
my-spring-boot-app/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/example/demo/
│   │   │       ├── DemoApplication.java
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── repository/
│   │   │       └── model/
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── static/
│   │       └── templates/
│   └── test/
├── pom.xml
└── README.md
```

#### 3. Application Properties
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=root123

# JPA Configuration
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

#### 4. Simple REST Controller
```java
@RestController
@RequestMapping("/api")
public class HelloController {
    
    @GetMapping("/hello")
    public String hello() {
        return "Hello, Spring Boot!";
    }
}
```

#### 5. Run Application
```bash
mvn spring-boot:run
```

Visit: `http://localhost:8080/api/hello`

---

## 📖 Learning Path

### Beginner Track
1. Start with **Spring Boot Overview** - Understand IoC and DI
2. Study **Spring Boot Fundamentals** - Learn configuration and profiles
3. Master **Spring Data JPA** - Database operations
4. Practice **Spring Web MVC** - Build web applications

### Intermediate Track
5. Learn **REST API Development** - Build RESTful services
6. Explore **Spring Security** - Secure your applications
7. Study **Testing** - Write comprehensive tests

### Advanced Track
8. Master **Microservices Architecture** - Distributed systems
9. Implement **Messaging and Integration** - Event-driven architecture
10. Learn **Batch Processing** - Handle large-scale data

---

## 🛠️ Common Dependencies

### Essential Starters
```xml
<!-- Web Applications -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Data Access -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Security -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>

<!-- Testing -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Lombok (Code Generation) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- MySQL Driver -->
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```

### Microservices Dependencies
```xml
<!-- Service Registry -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>

<!-- API Gateway -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>

<!-- Config Server -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-config-server</artifactId>
</dependency>

<!-- Feign Client -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-openfeign</artifactId>
</dependency>

<!-- Circuit Breaker -->
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

---

## 🎯 Key Concepts Reference

### Design Patterns
- **Singleton**: Default Spring bean scope
- **Factory**: Bean creation through factory methods
- **Proxy**: AOP and transaction management
- **Template Method**: JdbcTemplate, RestTemplate
- **Observer**: Event handling

### Annotations Quick Reference

#### Core Annotations
| Annotation | Purpose |
|-----------|---------|
| `@SpringBootApplication` | Main application class |
| `@Component` | Generic Spring component |
| `@Service` | Business logic layer |
| `@Repository` | Data access layer |
| `@Controller` | Web MVC controller |
| `@RestController` | REST API controller |
| `@Configuration` | Configuration class |
| `@Bean` | Bean definition method |

#### Dependency Injection
| Annotation | Purpose |
|-----------|---------|
| `@Autowired` | Auto-wire dependencies |
| `@Qualifier` | Specify bean name |
| `@Primary` | Primary bean when multiple exist |
| `@Value` | Inject property values |

#### Web Annotations
| Annotation | Purpose |
|-----------|---------|
| `@RequestMapping` | Map HTTP requests |
| `@GetMapping` | HTTP GET requests |
| `@PostMapping` | HTTP POST requests |
| `@PutMapping` | HTTP PUT requests |
| `@DeleteMapping` | HTTP DELETE requests |
| `@PathVariable` | Extract URL path variables |
| `@RequestParam` | Extract query parameters |
| `@RequestBody` | Bind request body to object |

#### JPA Annotations
| Annotation | Purpose |
|-----------|---------|
| `@Entity` | JPA entity class |
| `@Table` | Map to database table |
| `@Id` | Primary key |
| `@GeneratedValue` | Auto-generate ID |
| `@Column` | Map to table column |
| `@OneToMany` | One-to-many relationship |
| `@ManyToOne` | Many-to-one relationship |

#### Testing Annotations
| Annotation | Purpose |
|-----------|---------|
| `@SpringBootTest` | Integration test |
| `@WebMvcTest` | Test web layer |
| `@DataJpaTest` | Test JPA repositories |
| `@MockBean` | Mock Spring bean |
| `@Test` | JUnit test method |

---

## 🔧 Troubleshooting

### Common Issues

#### 1. Port Already in Use
```properties
# Change port in application.properties
server.port=8081
```

#### 2. Bean Creation Error
- Check for circular dependencies
- Verify component scanning package
- Ensure beans are properly annotated

#### 3. Database Connection Issues
```properties
# Verify connection details
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=root123
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
```

#### 4. Whitelabel Error Page
- Check controller mappings
- Verify URL patterns
- Review exception handling

---

## 📝 Best Practices

### Code Organization
1. **Package Structure**
   ```
   com.company.project/
   ├── controller/
   ├── service/
   ├── repository/
   ├── model/
   ├── dto/
   ├── config/
   └── exception/
   ```

2. **Naming Conventions**
   - Controllers: `*Controller`
   - Services: `*Service`
   - Repositories: `*Repository`
   - DTOs: `*DTO` or `*Request`/`*Response`

### Configuration Management
1. Use profiles for different environments
2. Externalize configuration
3. Use YAML for better readability
4. Keep secrets secure (environment variables)

### Security
1. Always hash passwords (BCrypt)
2. Enable HTTPS in production
3. Implement CSRF protection for web apps
4. Use JWT for stateless authentication
5. Validate all user inputs

### Performance
1. Use caching (Redis)
2. Implement pagination for large datasets
3. Use connection pooling
4. Optimize database queries
5. Enable gzip compression

### Testing
1. Write unit tests for business logic
2. Integration tests for API endpoints
3. Aim for 80%+ code coverage
4. Use test profiles
5. Mock external dependencies

---

## 🌟 Sample Projects

### 1. Simple CRUD Application
- Basic REST API
- MySQL database
- CRUD operations
- Exception handling

### 2. E-Commerce API
- Product catalog
- User authentication
- Order management
- Payment integration

### 3. Microservices System
- Service Registry
- API Gateway
- Multiple microservices
- Inter-service communication
- Circuit breaker

### 4. Real-Time Chat Application
- WebSocket integration
- Message queue (Kafka)
- Redis cache
- User authentication

---

## 📚 Additional Resources

### Official Documentation
- [Spring Boot Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/)
- [Spring Framework Reference](https://docs.spring.io/spring-framework/reference/)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [Spring Security](https://docs.spring.io/spring-security/reference/)

### Learning Resources
- [Spring Guides](https://spring.io/guides)
- [Baeldung Spring Tutorials](https://www.baeldung.com/spring-tutorial)
- [Spring Boot Examples](https://github.com/spring-projects/spring-boot/tree/main/spring-boot-samples)

### Tools
- [Spring Initializr](https://start.spring.io/)
- [Swagger UI](https://swagger.io/tools/swagger-ui/)
- [Postman](https://www.postman.com/)
- [Docker](https://www.docker.com/)

---

## 🤝 Contributing
Feel free to contribute to these notes by:
- Reporting issues
- Suggesting improvements
- Adding examples
- Correcting errors

---

## 📄 License
These notes are for educational purposes.

---

## ✨ Author
Created as a comprehensive learning resource for Spring Boot development.

**Happy Learning! 🚀**
