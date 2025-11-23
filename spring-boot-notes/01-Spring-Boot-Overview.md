# Spring Boot Comprehensive Notes

## Table of Contents
- [Microservices Architecture](#microservices-architecture)
- [Spring Core - IoC and Dependency Injection](#spring-core---ioc-and-dependency-injection)
- [Spring Boot Introduction](#spring-boot-introduction)

---

## Microservices Architecture

### Servers Used in Microservices Development

| Server | Purpose |
|--------|---------|
| **Service Registry** | Maintains list of available APIs (name, status, URL) |
| **Admin Server** | Dashboard to monitor and manage all APIs actuators |
| **API Gateway** | Entry point for all microservices, handles routing and filtering |

### Development Flow
```
Frontend → API Gateway → Backend Services → Database
```
- **API Gateway** acts as a mediator between Frontend and Backend
- Provides request verification and validation
- Any request to backend services should go through API Gateway

---

## Spring Core - IoC and Dependency Injection

### Core Concepts

#### Inversion of Control (IoC)
- **Principle**: Framework manages and collaborates objects (creating and injecting)
- **Spring Beans**: Classes managed by IoC container
- **Responsibility**: IOC manages dependencies among objects in the application

#### Dependency Injection (DI)
Injecting dependent object into target object using:
1. **Constructor Injection** (Recommended)
2. **Setter Injection**
3. **Field Injection** (Only with annotations)

**Note**: If both constructor and setter injection are performed, constructor injection happens first, then setter injection overrides the value.

### Types of IoC Containers

| Container Type | Status | Interface |
|---------------|--------|-----------|
| BeanFactory | Outdated | - |
| ApplicationContext | Current | ✅ |

```java
ApplicationContext ctx = new ClassPathXmlApplicationContext(String xmlFilePath);
```

### XML Configuration Example

**Beans.xml** (should be created in `src/main/java`):

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        http://www.springframework.org/schema/beans 
        http://www.springframework.org/schema/beans/spring-beans.xsd">

    <!-- Bean Definitions -->
    <bean id="credit" class="com.alchemist.beans.CreditCardPayment"/>
    <bean id="debit" class="com.alchemist.beans.DebitCardPayment"/>
    
    <!-- Constructor Injection -->
    <bean id="payment" class="com.alchemist.beans.PaymentService">
       <constructor-arg name="payment" ref="debit"/>
    </bean>
    
    <!-- Setter Injection -->
    <bean id="payment" class="com.alchemist.beans.PaymentService">
       <property name="iPayment" ref="credit"/>
    </bean>
</beans>
```

**Note**: `ref` attribute represents which object should be injected

### Spring Bean Scope

| Scope | Behavior | Object Creation Timing |
|-------|----------|----------------------|
| **Singleton** (Default) | One instance per container | At container startup |
| **Prototype** | New instance each time | When `getBean()` is called |
| **Request** | One per HTTP request | During request |
| **Session** | One per HTTP session | During session |

**Note**: Request and Session scopes are used in Spring Web MVC

```xml
<bean id="car" class="com.alchemist.beans.CarService" scope="prototype">
   <constructor-arg name="car" ref="petrol"/>
</bean>
```

### Autowiring

**Manual Wiring**: Injecting dependent object with `ref` attribute

**Autowiring**: Automatically identify and inject objects

#### Autowiring Modes

| Mode | Type | Description |
|------|------|-------------|
| **byName** | Setter | Matches bean id with variable name |
| **byType** | Setter | Matches variable type |
| **constructor** | Constructor | First byName, then byType |
| **none** | - | Disabled |

#### byName Example
```xml
<bean id="car" class="com.alchemist.beans.CarService" autowire="byName"/>
```

#### byType Example
```xml
<bean id="engine" class="com.alchemist.beans.PetrolEngine" autowire-candidate="false"/>
<bean id="engine1" class="com.alchemist.beans.DieselEngine"/>
<bean id="car" class="com.alchemist.beans.CarService" autowire="byType"/>
```

Or using `primary`:
```xml
<bean id="engine" class="com.alchemist.beans.PetrolEngine" primary="true"/>
<bean id="engine1" class="com.alchemist.beans.DieselEngine"/>
<bean id="car" class="com.alchemist.beans.CarService" autowire="byType"/>
```

### Spring Annotations

#### Configuration Annotations

| Annotation | Purpose |
|-----------|---------|
| `@Configuration` | Marks class as configuration (replaces XML) |
| `@Component` | Generic stereotype for Spring-managed component |
| `@Service` | Business logic layer |
| `@Repository` | Data access layer (DAO) |
| `@Controller` | Presentation layer |
| `@Scope` | Defines bean scope |
| `@ComponentScan` | Scans for components in base packages |

#### Dependency Injection Annotations

| Annotation | Purpose |
|-----------|---------|
| `@Autowired` | Enable auto-wiring (field/constructor/setter) |
| `@Qualifier` | Specify bean by name when multiple exist |
| `@Primary` | Give priority for auto-wiring |
| `@Bean` | Declare method produces bean for IoC |

#### Example: Java Configuration

```java
@Configuration
@ComponentScan(basePackages = "com.alchemist")
public class AppConfig {
    // Configuration beans
}
```

#### Autowiring with Annotations

**1. Field Injection**
```java
@Service
public class ReportService {
    @Autowired
    private ReportDAO reportDAO;
}
```

**2. Autowiring by Name**
```java
@Repository("reportDAO")
public class OracleDBReportDAO implements ReportDAO {
    // Implementation
}
```

**3. Using @Qualifier**
```java
@Service
public class ReportService {
    @Autowired
    @Qualifier("oracleDBDAO")
    private ReportDAO reportDAO;
}
```

**4. Using @Primary**
```java
@Repository("oracleDBDAO")
@Primary
public class OracleDBDAO implements ReportDAO {
    // Implementation
}
```

**5. Setter Injection**
```java
@Service
public class ReportService {
    private ReportDAO reportDAO;
    
    @Autowired
    public void setReportDAO(@Qualifier("oracleDBDAO") ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }
}
```

**6. Constructor Injection** (Recommended)
```java
@Service
public class ReportService {
    private ReportDAO reportDAO;
    
    @Autowired  // Optional if only one constructor
    public ReportService(@Qualifier("mysqlDBDAO") ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }
}
```

**Note**: If class has only one parameterized constructor, `@Autowired` is optional.

### Spring Bean Life-Cycle

Three ways to execute lifecycle methods:

#### 1. Declarative Approach (XML)
```xml
<bean id="dao" class="com.alchemist.UserDAO"
    init-method="init"
    destroy-method="destroy"/>
```

#### 2. Programmatic Approach (Interfaces)
```java
public class UserDAO implements InitializingBean, DisposableBean {
    
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("Getting db connection.....");
    }
    
    public void getData() {
        System.out.println("Getting the data from the db...");
    }
    
    @Override
    public void destroy() throws Exception {
        System.out.println("Closing db connection.....");
    }
}
```

#### 3. Annotation Approach (Recommended)
```java
@Component
public class UserDAO {
    
    @PostConstruct
    public void init() {
        System.out.println("Getting db connection.....");
    }
    
    public void getData() {
        System.out.println("Getting the data from the db...");
    }
    
    @PreDestroy
    public void destroy() {
        System.out.println("Closing db connection.....");
    }
}
```

#### Main Class Example
```java
@Configuration
@ComponentScan(basePackages = "com.alchemist")
public class Application {
    public static void main(String[] args) {
        ApplicationContext context = 
            new AnnotationConfigApplicationContext(AppConfig.class);
        
        UserDAO dao = context.getBean(UserDAO.class);
        dao.getData();
        
        // To see destroy() execution
        ConfigurableApplicationContext ctxt = 
            (ConfigurableApplicationContext) context;
        ctxt.close();
    }
}
```

### @DependsOn Annotation

Used when one bean depends on another bean's initialization.

```java
@Component("userDao")
public class UserDao implements InitializingBean, DisposableBean {
    // Implementation
}

@Service
@DependsOn("userDao")
public class UserService {
    public UserService() {
        System.out.println("Getting data from Redis...");
    }
}
```

**Use Case**: When Class A depends on Class B completing its setup (e.g., B stores data in Redis, A reads from Redis).

**Note**: Without `@DependsOn`, beans may be created in alphabetical order, causing issues.

---

## Spring Boot Introduction

### What is Spring Boot?

Spring Boot is an extension of Spring Framework that simplifies Spring-based application development.

**Formula**: 
```
Spring Boot = Spring Framework 
            - XML Configuration 
            + Auto Configuration 
            + Embedded Servers 
            + Actuator
```

### Features

1. **Starter POM** - Simplifies Maven/Gradle dependencies
   - `spring-boot-starter-web`
   - `spring-boot-starter-data-jpa`
   - `spring-boot-starter-security`
   - `spring-boot-starter-mail`

2. **Auto Configuration** - Common configurations handled automatically
   - Database connection pools
   - Embedded server deployment
   - IoC container startup
   - Component scanning

3. **Embedded Servers** - No external server needed
   - Apache Tomcat (default)
   - Jetty
   - Netty

4. **Actuator** - Production-ready features for monitoring

### Spring Boot Banner

#### Banner Modes

| Mode | Behavior |
|------|----------|
| **Console** (default) | Prints on console |
| **Log** | Prints to log file |
| **Off** | Disabled |

#### Custom Banner

Create `banner.txt` in `src/main/resources`:

```text
  ____             _             _       
 / ___| _ __  _ __(_)_ __  _   _| |_ ___ 
 \___ \| '_ \| '__| | '_ \| | | | __/ _ \
  ___) | |_) | |  | | | | | |_| | ||  __/
 |____/| .__/|_|  |_|_| |_|\__,_|\__\___|
       |_|        ${spring-boot.version}

Application: ${spring.application.name}
Profile: ${spring.profiles.active}
```

#### Disable Banner

**application.properties**:
```properties
spring.main.banner-mode=off
```

**Or programmatically**:
```java
@SpringBootApplication
public class MyApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(MyApplication.class);
        app.setBannerMode(Banner.Mode.OFF);
        app.run(args);
    }
}
```

### @SpringBootApplication Annotation

Combines three annotations:

```java
@SpringBootApplication = 
    @SpringBootConfiguration    // = @Configuration
  + @EnableAutoConfiguration    // Auto-configuration
  + @ComponentScan              // Component scanning
```

### Spring Boot Internals

#### SpringApplication.run() Flow

```
SpringApplication.run(MyApp.class, args)
    │
    ├── Create SpringApplication object
    ├── Prepare Environment
    ├── Print Banner
    ├── Create ApplicationContext
    ├── Prepare Context
    ├── Refresh Context (Bean creation)
    ├── Run CommandLineRunner / ApplicationRunner
    └── Application is READY ✅
```

#### ApplicationContext Types

| Dependency | Context Class |
|-----------|---------------|
| `spring-boot-starter-web` | `AnnotationConfigServletWebServerApplicationContext` |
| `spring-boot-starter` | `AnnotationConfigApplicationContext` |
| `spring-boot-starter-webflux` | `AnnotationConfigReactiveWebServerApplicationContext` |

**Note**: If both `web-starter` and `webflux` are present, `web-starter` takes priority.

---

## Project Lombok

Reduces boilerplate code by generating methods at compile time.

### Dependency

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.38</version>
</dependency>
```

### Common Annotations

| Annotation | Purpose |
|-----------|---------|
| `@Getter` / `@Setter` | Generate getters/setters |
| `@ToString` | Generate `toString()` |
| `@EqualsAndHashCode` | Generate `equals()` and `hashCode()` |
| `@NoArgsConstructor` | No-argument constructor |
| `@AllArgsConstructor` | Constructor with all fields |
| `@RequiredArgsConstructor` | Constructor for final fields |
| `@Data` | Combines @Getter, @Setter, @ToString, @EqualsAndHashCode, @RequiredArgsConstructor |
| `@Builder` | Builder pattern |
| `@Value` | Immutable class |
| `@Slf4j` | Logger instance |

### Example

```java
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Customer {
    private Integer id;
    private String name;
    private String email;
    private Long phone;
}
```

---

*Continue to [02-Spring-Boot-Fundamentals.md](02-Spring-Boot-Fundamentals.md) for detailed Spring Boot features.*
