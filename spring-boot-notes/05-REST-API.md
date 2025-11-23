# Spring Boot REST API

## Table of Contents
- [REST Fundamentals](#rest-fundamentals)
- [HTTP Methods](#http-methods)
- [HTTP Status Codes](#http-status-codes)
- [REST Controller](#rest-controller)
- [Request/Response Handling](#requestresponse-handling)
- [Content Negotiation](#content-negotiation)
- [Swagger Documentation](#swagger-documentation)
- [REST Client Development](#rest-client-development)

---

## REST Fundamentals

### What is REST?

**REST** (Representational State Transfer) is an architectural style for designing networked applications.

### Key Principles

1. **Client-Server Architecture**: Separation of concerns
2. **Stateless**: Each request is independent
3. **Cacheable**: Responses can be cached
4. **Uniform Interface**: Standardized communication
5. **Layered System**: Hierarchical layers

### REST vs SOAP

| Feature | REST | SOAP |
|---------|------|------|
| **Protocol** | HTTP | Multiple protocols |
| **Data Format** | JSON, XML | XML only |
| **Complexity** | Simple | Complex |
| **Performance** | Faster | Slower |
| **State** | Stateless | Can be stateful |
| **Security** | HTTPS | WS-Security |

### REST API Components

```
REST API = REST Controller + Business Logic + DAO + Database
```

**Flow**:
```
Client (Postman/Browser) → REST Controller → Service Layer → DAO → Database
                         ← JSON/XML Response ←
```

---

## HTTP Methods

### Standard HTTP Methods

| Method | Purpose | Idempotent | Safe |
|--------|---------|-----------|------|
| **GET** | Retrieve resources | ✅ Yes | ✅ Yes |
| **POST** | Create new resource | ❌ No | ❌ No |
| **PUT** | Update/Replace resource | ✅ Yes | ❌ No |
| **PATCH** | Partial update | ❌ No | ❌ No |
| **DELETE** | Remove resource | ✅ Yes | ❌ No |

### HTTP Method Details

#### GET
- Retrieves data from server
- No request body
- Parameters in URL
- Example: `GET /api/users?page=1&size=10`

#### POST
- Creates new resource
- Has request body
- Example: `POST /api/users` with JSON body

#### PUT
- Updates entire resource
- Replaces existing resource
- Example: `PUT /api/users/123` with complete user data

#### PATCH
- Partial update
- Updates specific fields
- Example: `PATCH /api/users/123` with `{"email": "new@email.com"}`

#### DELETE
- Removes resource
- Example: `DELETE /api/users/123`

---

## HTTP Status Codes

### Status Code Categories

| Range | Category | Meaning |
|-------|----------|---------|
| **1xx** | Informational | Request received, processing |
| **2xx** | Success | Request successfully processed |
| **3xx** | Redirection | Further action needed |
| **4xx** | Client Error | Invalid request from client |
| **5xx** | Server Error | Server failed to process valid request |

### Common Status Codes

#### Success (2xx)

| Code | Name | Usage |
|------|------|-------|
| **200** | OK | Successful GET/PUT/PATCH request |
| **201** | Created | Resource created successfully (POST) |
| **204** | No Content | Successful DELETE (no response body) |

#### Redirection (3xx)

| Code | Name | Usage |
|------|------|-------|
| **301** | Moved Permanently | Resource permanently moved |
| **302** | Found | Temporary redirect |
| **304** | Not Modified | Cached version is still valid |

#### Client Errors (4xx)

| Code | Name | Usage |
|------|------|-------|
| **400** | Bad Request | Invalid request syntax/data |
| **401** | Unauthorized | Authentication required |
| **403** | Forbidden | Authenticated but not authorized |
| **404** | Not Found | Resource doesn't exist |
| **405** | Method Not Allowed | HTTP method not supported |
| **409** | Conflict | Request conflicts with current state |

#### Server Errors (5xx)

| Code | Name | Usage |
|------|------|-------|
| **500** | Internal Server Error | Generic server error |
| **501** | Not Implemented | Server doesn't support functionality |
| **503** | Service Unavailable | Server temporarily unavailable |

---

## REST Controller

### Dependencies

```xml
<dependencies>
    <!-- Spring Web (includes REST support) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- Spring Data JPA -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <!-- MySQL Driver -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <!-- Lombok -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

### Basic REST Controller

```java
@RestController
@RequestMapping("/api/books")
public class BookRestController {
    
    @Autowired
    private BookService service;
    
    // POST - Create book
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody Book book) {
        String msg = service.upsert(book);
        return new ResponseEntity<>(msg, HttpStatus.CREATED);
    }
    
    // GET - Get all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> allBooks = service.getAllBooks();
        return new ResponseEntity<>(allBooks, HttpStatus.OK);
    }
    
    // GET - Get book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBook(@PathVariable Integer id) {
        Book book = service.getById(id);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }
    
    // PUT - Update book
    @PutMapping
    public ResponseEntity<String> updateBook(@RequestBody Book book) {
        String msg = service.upsert(book);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
    
    // DELETE - Delete book
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Integer id) {
        String msg = service.deleteById(id);
        return new ResponseEntity<>(msg, HttpStatus.OK);
    }
}
```

### @Controller vs @RestController

```java
// Traditional Controller
@Controller
public class MyController {
    @GetMapping("/api/data")
    @ResponseBody  // Required for REST response
    public String getData() {
        return "Data";
    }
}

// REST Controller (cleaner)
@RestController  // = @Controller + @ResponseBody on all methods
public class MyRestController {
    @GetMapping("/api/data")
    public String getData() {
        return "Data";
    }
}
```

---

## Request/Response Handling

### Request Annotations

#### @RequestBody

Binds HTTP request body to Java object (deserialization).

```java
@PostMapping("/users")
public ResponseEntity<User> createUser(@RequestBody User user) {
    User savedUser = userService.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
}
```

**Request**:
```json
POST /api/users
Content-Type: application/json

{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30
}
```

#### @PathVariable

Extracts value from URI path.

```java
@GetMapping("/users/{id}")
public ResponseEntity<User> getUser(@PathVariable("id") Long userId) {
    User user = userService.findById(userId);
    return ResponseEntity.ok(user);
}
```

**Request**: `GET /api/users/123`

#### @RequestParam

Extracts query parameters.

```java
@GetMapping("/users")
public ResponseEntity<List<User>> searchUsers(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "10") int size) {
    
    List<User> users = userService.search(name, page, size);
    return ResponseEntity.ok(users);
}
```

**Request**: `GET /api/users?name=John&page=1&size=20`

#### @RequestHeader

Extracts HTTP headers.

```java
@GetMapping("/data")
public ResponseEntity<String> getData(
        @RequestHeader("Authorization") String authToken,
        @RequestHeader(value = "User-Agent", required = false) String userAgent) {
    
    // Validate token, process request
    return ResponseEntity.ok("Data");
}
```

### Response Handling with ResponseEntity

```java
@RestController
@RequestMapping("/api/products")
public class ProductRestController {
    
    // Return with custom status
    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        Product saved = service.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
    
    // Return with headers
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProduct(@PathVariable Long id) {
        Product product = service.findById(id);
        
        HttpHeaders headers = new HttpHeaders();
        headers.add("Custom-Header", "value");
        
        return ResponseEntity.ok()
                .headers(headers)
                .body(product);
    }
    
    // Return without body
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    
    // Handle not found
    @GetMapping("/search/{name}")
    public ResponseEntity<Product> searchProduct(@PathVariable String name) {
        Optional<Product> product = service.findByName(name);
        
        return product.map(ResponseEntity::ok)
                      .orElse(ResponseEntity.notFound().build());
    }
}
```

### ResponseEntity Builder Methods

| Method | Status Code | Usage |
|--------|-------------|-------|
| `ResponseEntity.ok()` | 200 OK | Successful GET/PUT |
| `ResponseEntity.created(URI)` | 201 Created | Resource created |
| `ResponseEntity.accepted()` | 202 Accepted | Async processing |
| `ResponseEntity.noContent()` | 204 No Content | Successful DELETE |
| `ResponseEntity.badRequest()` | 400 Bad Request | Invalid input |
| `ResponseEntity.notFound()` | 404 Not Found | Resource missing |
| `ResponseEntity.status(code)` | Custom | Any status code |

---

## Content Negotiation

### JSON Support (Default)

Spring Boot uses **Jackson** for JSON by default.

**Request**:
```
GET /api/books/1
Accept: application/json
```

**Response**:
```json
{
    "bookId": 1,
    "bookName": "Spring Boot in Action",
    "bookPrice": 500.0
}
```

### XML Support

#### Add Jackson XML Dependency

```xml
<dependency>
    <groupId>com.fasterxml.jackson.dataformat</groupId>
    <artifactId>jackson-dataformat-xml</artifactId>
</dependency>
```

#### Entity Configuration

```java
@Data
@Entity
@Table(name = "BOOK_TBL")
@XmlRootElement  // Enable XML serialization
public class Book {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;
    
    private String bookName;
    
    private Double bookPrice;
}
```

#### XML Request/Response

**Request**:
```
GET /api/books/1
Accept: application/xml
```

**Response**:
```xml
<Book>
    <bookId>1</bookId>
    <bookName>Spring Boot in Action</bookName>
    <bookPrice>500.0</bookPrice>
</Book>
```

### Content Type in Controller

```java
@RestController
@RequestMapping("/api/books")
public class BookController {
    
    // Produces JSON only
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book getBookJson(@PathVariable Integer id) {
        return service.getBook(id);
    }
    
    // Produces XML only
    @GetMapping(value = "/{id}/xml", produces = MediaType.APPLICATION_XML_VALUE)
    public Book getBookXml(@PathVariable Integer id) {
        return service.getBook(id);
    }
    
    // Produces both JSON and XML (based on Accept header)
    @GetMapping(value = "/{id}/any", 
                produces = {MediaType.APPLICATION_JSON_VALUE, 
                           MediaType.APPLICATION_XML_VALUE})
    public Book getBookAny(@PathVariable Integer id) {
        return service.getBook(id);
    }
}
```

---

## Swagger Documentation

### Why Swagger?

- **API Documentation**: Auto-generates interactive documentation
- **API Testing**: Test endpoints directly from browser
- **Client Code Generation**: Generate client libraries
- **Collaboration**: Share API specs with frontend teams

### Dependencies (SpringDoc OpenAPI)

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.0.2</version>
</dependency>
```

### Configuration (Optional)

```properties
# application.properties
springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

### Accessing Swagger UI

1. Start application
2. Open browser: `http://localhost:8080/swagger-ui.html`
3. View and test all endpoints

### Swagger Annotations

```java
@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users")
public class UserController {
    
    @Operation(summary = "Get all users", description = "Retrieves a list of all users")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.findAll());
    }
    
    @Operation(summary = "Create user", description = "Creates a new user")
    @PostMapping
    public ResponseEntity<User> createUser(
            @Parameter(description = "User object to be created")
            @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.save(user));
    }
    
    @Operation(summary = "Get user by ID")
    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @Parameter(description = "User ID", example = "123")
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }
}
```

### Entity Documentation

```java
@Data
@Entity
@Schema(description = "User entity representing application users")
public class User {
    
    @Schema(description = "Unique identifier", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Schema(description = "User's full name", example = "John Doe", required = true)
    @NotBlank
    private String name;
    
    @Schema(description = "User's email address", example = "john@example.com")
    @Email
    private String email;
    
    @Schema(description = "User's age", example = "30", minimum = "18", maximum = "100")
    @Min(18)
    @Max(100)
    private Integer age;
}
```

---

## REST Client Development

### RestTemplate (Legacy)

```java
@Service
public class QuoteService {
    
    private RestTemplate restTemplate = new RestTemplate();
    
    public Quote getQuote() {
        String url = "https://api.example.com/quote";
        ResponseEntity<Quote> response = restTemplate.getForEntity(url, Quote.class);
        return response.getBody();
    }
    
    public void createQuote(Quote quote) {
        String url = "https://api.example.com/quotes";
        restTemplate.postForEntity(url, quote, Quote.class);
    }
}
```

### WebClient (Recommended)

#### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

#### WebClient Configuration

```java
@Configuration
public class WebClientConfig {
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://api.example.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}
```

#### Using WebClient

```java
@Service
public class QuoteService {
    
    @Autowired
    private WebClient webClient;
    
    // GET request
    public Mono<Quote> getQuote() {
        return webClient.get()
                .uri("/quote")
                .retrieve()
                .bodyToMono(Quote.class);
    }
    
    // GET with path variable
    public Mono<Quote> getQuoteById(Long id) {
        return webClient.get()
                .uri("/quotes/{id}", id)
                .retrieve()
                .bodyToMono(Quote.class);
    }
    
    // POST request
    public Mono<Quote> createQuote(Quote quote) {
        return webClient.post()
                .uri("/quotes")
                .bodyValue(quote)
                .retrieve()
                .bodyToMono(Quote.class);
    }
    
    // PUT request
    public Mono<Quote> updateQuote(Long id, Quote quote) {
        return webClient.put()
                .uri("/quotes/{id}", id)
                .bodyValue(quote)
                .retrieve()
                .bodyToMono(Quote.class);
    }
    
    // DELETE request
    public Mono<Void> deleteQuote(Long id) {
        return webClient.delete()
                .uri("/quotes/{id}", id)
                .retrieve()
                .bodyToMono(Void.class);
    }
    
    // Handle errors
    public Mono<Quote> getQuoteSafe(Long id) {
        return webClient.get()
                .uri("/quotes/{id}", id)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, 
                          response -> Mono.error(new RuntimeException("Client error")))
                .onStatus(HttpStatus::is5xxServerError,
                          response -> Mono.error(new RuntimeException("Server error")))
                .bodyToMono(Quote.class);
    }
}
```

### Testing REST APIs

#### Using Postman

1. **GET Request**:
   - Method: GET
   - URL: `http://localhost:8080/api/books`
   - Headers: `Accept: application/json`

2. **POST Request**:
   - Method: POST
   - URL: `http://localhost:8080/api/books`
   - Headers: `Content-Type: application/json`
   - Body (raw JSON):
   ```json
   {
       "bookName": "Effective Java",
       "bookPrice": 450.0
   }
   ```

3. **PUT Request**:
   - Method: PUT
   - URL: `http://localhost:8080/api/books`
   - Body: Updated book JSON

4. **DELETE Request**:
   - Method: DELETE
   - URL: `http://localhost:8080/api/books/1`

#### Using cURL

```bash
# GET
curl -X GET http://localhost:8080/api/books

# POST
curl -X POST http://localhost:8080/api/books \
     -H "Content-Type: application/json" \
     -d '{"bookName":"Effective Java","bookPrice":450.0}'

# PUT
curl -X PUT http://localhost:8080/api/books \
     -H "Content-Type: application/json" \
     -d '{"bookId":1,"bookName":"Effective Java 3rd Edition","bookPrice":500.0}'

# DELETE
curl -X DELETE http://localhost:8080/api/books/1
```

---

*Continue to [06-Microservices.md](06-Microservices.md) for Microservices architecture.*
