# Spring Boot Fundamentals

## Table of Contents
- [Spring Boot Runners](#spring-boot-runners)
- [Spring Boot Actuator](#spring-boot-actuator)
- [Embedded Servers](#embedded-servers)
- [Profiles in Spring Boot](#profiles-in-spring-boot)
- [Properties vs YAML](#properties-vs-yaml)

---

## Spring Boot Runners

Runners allow you to execute code once the application context is fully loaded but before the application starts accepting requests.

### Types of Runners

| Runner Type | Interface Method | Parameter Type |
|------------|------------------|----------------|
| **CommandLineRunner** | `run(String... args)` | String array |
| **ApplicationRunner** | `run(ApplicationArguments args)` | ApplicationArguments |

### Use Cases
- Load static table data when application starts
- Delete data from staging/temporary tables
- Send notification regarding application startup
- Initial data validation

### Startup Sequence

```
Spring Boot Startup:
├── Context Refresh (Bean creation, Dependency Injection)
├── Web Server Starts
├── ApplicationStartedEvent Published
├── 🏃 Runners Execute (in @Order sequence)
│   ├── CommandLineRunner @Order(1)
│   ├── ApplicationRunner @Order(2)
│   └── Other runners...
└── ApplicationReadyEvent Published (App is READY!)
```

### Example: ApplicationRunner

```java
@Component
public class MyApplicationRunner implements ApplicationRunner {
    
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("ApplicationRunner run() method executed");
        // Load static data, perform startup tasks
    }
}
```

### Example: CommandLineRunner

```java
@Component
@Order(1)
public class MyCommandLineRunner implements CommandLineRunner {
    
    @Override
    public void run(String... args) throws Exception {
        System.out.println("CommandLineRunner run() method executed");
        System.out.println("Arguments: " + Arrays.toString(args));
    }
}
```

### Execution Order

Use `@Order` annotation to control execution sequence:

```java
@Component
@Order(1)
public class FirstRunner implements CommandLineRunner {
    public void run(String... args) {
        System.out.println("First runner");
    }
}

@Component
@Order(2)
public class SecondRunner implements ApplicationRunner {
    public void run(ApplicationArguments args) {
        System.out.println("Second runner");
    }
}
```

**Note**: Lower order value = higher priority

---

## Spring Boot Actuator

Production-ready features for monitoring and managing applications.

### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

### Default Endpoint

```
http://localhost:8080/actuator
```

### Common Endpoints

| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health (UP/DOWN) |
| `/actuator/info` | Custom app info (version, name) |
| `/actuator/metrics` | Performance metrics (memory, CPU, GC) |
| `/actuator/beans` | All Spring Beans loaded |
| `/actuator/env` | Environment variables and properties |
| `/actuator/mappings` | All HTTP mappings |
| `/actuator/loggers` | View and change log levels at runtime |

### Configuration

**application.properties**:
```properties
# Expose all endpoints
management.endpoints.web.exposure.include=*

# Change base path
management.endpoints.web.base-path=/manage

# Custom info endpoint data
info.app.name=PayrollApp
info.app.version=1.0.0
info.app.owner=Rohit Lavate
```

**application.yml**:
```yaml
management:
  endpoints:
    web:
      exposure:
        include: '*'        # Expose all endpoints
        exclude: 'mappings' # Exclude specific endpoint
  endpoint:
    shutdown:
      enabled: true        # Enable shutdown endpoint
```

### Shutdown Endpoint

Special endpoint to stop application (mapped to POST request):

```bash
# POST request to shutdown
curl -X POST http://localhost:8080/actuator/shutdown
```

### Use Case Example

In production applications (like payroll/compliance systems):
- `/actuator/health` → Used by load balancer to check if service is alive
- `/actuator/metrics/jvm.memory.used` → Used by Grafana to monitor memory
- `/actuator/loggers` → Allows changing log level without restarting app

---

## Embedded Servers

Spring Boot includes embedded servers - no need for external Tomcat/Jetty installation.

### Server Comparison

| Feature | Tomcat | Jetty | Netty |
|---------|--------|-------|-------|
| **Type** | Servlet container | Servlet container | NIO framework |
| **Spring Boot default** | ✅ Yes | ❌ No | ❌ No |
| **Performance** | Good | Very good | Excellent |
| **Concurrency** | Medium | High | Very high |
| **Blocking model** | Blocking | Async (supports non-blocking) | Fully non-blocking |
| **Footprint** | Moderate | Small | Small |
| **Reactive support** | ❌ | Partial | ✅ Full |
| **Ideal for** | REST APIs, web apps | Microservices | Reactive, event-driven systems |

### Default Ports

- Tomcat: `8080`
- Netty (WebFlux): `8080`

### Change Server Port

**application.properties**:
```properties
server.port=9090
server.servlet.context-path=/myapp
```

**application.yml**:
```yaml
server:
  port: 9090
  servlet:
    context-path: /myapp
```

### Switch to Jetty

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
    <exclusions>
        <exclusion>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-tomcat</artifactId>
        </exclusion>
    </exclusions>
</dependency>

<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-jetty</artifactId>
</dependency>
```

---

## Profiles in Spring Boot

Profiles are used to configure environment-specific properties.

### Environment Types

| Environment | Purpose |
|------------|---------|
| **DEV** | Development environment |
| **SIT** | System Integration Testing |
| **STG** | Staging environment |
| **UAT** | User Acceptance Testing |
| **PROD** | Production environment |

### Why Profiles?

Without profiles:
- Manual changes to `application.properties` for each environment
- Risk of mistakes during deployment
- Tight coupling with environment configuration

With profiles:
- Separate configuration per environment
- No code changes during deployment
- Environment-specific database, SMTP, Kafka, Redis properties

### File Structure

```
src/main/resources/
 ├── application.properties              # Default (active if no profile set)
 ├── application-dev.properties          # For dev
 ├── application-test.properties         # For testing
 └── application-prod.properties         # For production
```

### Example Configurations

**application-dev.properties**:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/dev_db
spring.datasource.username=dev_user
spring.datasource.password=dev_pass
```

**application-prod.properties**:
```properties
spring.datasource.url=jdbc:mysql://prod-server:3306/prod_db
spring.datasource.username=prod_user
spring.datasource.password=${PROD_DB_PASSWORD}
```

### Activate Profile

**1. In application.properties**:
```properties
spring.profiles.active=dev
```

**2. In application.yml**:
```yaml
spring:
  profiles:
    active: dev
```

**3. Command Line**:
```bash
java -jar myapp.jar --spring.profiles.active=prod
```

**4. Environment Variable**:
```bash
export SPRING_PROFILES_ACTIVE=prod
java -jar myapp.jar
```

**5. IDE Configuration**:
```
-Dspring.profiles.active=dev
```

### Profile-Specific Beans

```java
@Configuration
@Profile("dev")
public class DevConfig {
    
    @Bean
    public DataSource devDataSource() {
        // Development datasource configuration
    }
}

@Configuration
@Profile("prod")
public class ProdConfig {
    
    @Bean
    public DataSource prodDataSource() {
        // Production datasource configuration
    }
}
```

---

## Properties vs YAML

### Properties File Format

```properties
# Key-value format
server.port=9090
spring.application.name=MyApp
spring.datasource.url=jdbc:mysql://localhost:3306/mydb
spring.datasource.username=root
spring.datasource.password=admin123

# Arrays/Lists
spring.profiles.active=dev,test
```

### YAML File Format

```yaml
# Hierarchical format
server:
  port: 9090

spring:
  application:
    name: MyApp
  datasource:
    url: jdbc:mysql://localhost:3306/mydb
    username: root
    password: admin123
  profiles:
    active: dev

# Lists
hobbies:
  - singing
  - playing
  - dancing
```

### Comparison

| Feature | Properties | YAML |
|---------|-----------|------|
| **Format** | Key-value (flat) | Hierarchical |
| **Readability** | Less readable | More readable |
| **Usage** | Java applications only | Universal (Java, Ansible, K8s, Python) |
| **Lists** | Comma-separated | Hyphen-prefixed |
| **Indentation** | Not important | Very important (spaces, not tabs) |
| **Comments** | `#` | `#` |

### YAML Advantages

1. **Better Structure**: Hierarchical representation
2. **Less Repetition**: No need to repeat prefixes
3. **Universal**: Works across multiple technologies
4. **Multiple Documents**: Can contain multiple configurations in one file

### YAML Syntax Rules

```yaml
# ✅ Correct - uses spaces for indentation
server:
  port: 9090
  servlet:
    context-path: /myapp

# ❌ Wrong - inconsistent indentation
server:
 port: 9090
   servlet:
  context-path: /myapp

# List format
hobbies:
  - singing
  - playing
  - dancing

# Inline list
hobbies: [singing, playing, dancing]

# Multi-line string
description: |
  This is a long
  multi-line string
  in YAML format
```

### Converting Properties to YAML

**application.properties**:
```properties
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/sbms
spring.datasource.username=root
spring.datasource.password=admin123
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
server.port=9090
```

**application.yml** (equivalent):
```yaml
spring:
  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/sbms
    username: root
    password: admin123
  jpa:
    show-sql: true
    hibernate:
      ddl-auto: update

server:
  port: 9090
```

### Profile-Specific YAML

```yaml
# Common configuration
spring:
  application:
    name: MyApp

---
# Dev profile
spring:
  config:
    activate:
      on-profile: dev
  datasource:
    url: jdbc:mysql://localhost:3306/dev_db

---
# Prod profile
spring:
  config:
    activate:
      on-profile: prod
  datasource:
    url: jdbc:mysql://prod-server:3306/prod_db
```

---

## Spring Boot Auto-Configuration

### How It Works

1. **Classpath Scanning**: Checks what dependencies are in classpath
2. **Conditional Beans**: Creates beans based on conditions
3. **Default Configuration**: Provides sensible defaults

### Example: DataSource Auto-Configuration

If you have:
- `spring-boot-starter-data-jpa` in dependencies
- Database driver in classpath
- Database properties configured

Spring Boot automatically:
- Creates DataSource bean
- Configures Connection Pool
- Sets up EntityManagerFactory
- Enables Transaction Management

### Key Files

| File | Purpose |
|------|---------|
| `spring-boot-autoconfigure-x.x.x.jar` | Contains all pre-written configuration classes |
| `META-INF/spring.factories` | Lists auto-configuration classes |

### Disable Auto-Configuration

```java
@SpringBootApplication(exclude = {
    DataSourceAutoConfiguration.class,
    HibernateJpaAutoConfiguration.class
})
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication.run(MyApplication.class, args);
    }
}
```

### View Auto-Configuration Report

**application.properties**:
```properties
debug=true
```

This will print:
- Positive matches (configurations applied)
- Negative matches (configurations not applied)
- Exclusions
- Unconditional classes

---

*Continue to [03-Spring-Data-JPA.md](03-Spring-Data-JPA.md) for database operations.*
