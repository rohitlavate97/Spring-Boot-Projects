# Spring Boot Testing

## Table of Contents
- [Testing Fundamentals](#testing-fundamentals)
- [Unit Testing](#unit-testing)
- [Integration Testing](#integration-testing)
- [REST API Testing](#rest-api-testing)
- [Mocking with Mockito](#mocking-with-mockito)
- [Test Coverage](#test-coverage)

---

## Testing Fundamentals

### Testing Pyramid

```
        /\
       /  \        E2E Tests (Few)
      /────\
     /      \      Integration Tests (Some)
    /────────\
   /          \    Unit Tests (Many)
  /────────────\
```

### Test Types

| Type | Scope | Speed | Dependencies |
|------|-------|-------|--------------|
| **Unit Test** | Single class | Fast | None (mocked) |
| **Integration Test** | Multiple components | Medium | Some (real) |
| **E2E Test** | Full application | Slow | All (real) |

### Dependencies

```xml
<dependencies>
    <!-- Spring Boot Test Starter -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <!-- Includes:
         - JUnit 5
         - Mockito
         - AssertJ
         - Hamcrest
         - Spring Test
    -->
</dependencies>
```

---

## Unit Testing

### JUnit 5 Basics

#### Test Structure

```java
@Test
void methodName_scenario_expectedBehavior() {
    // Arrange (Given) - Setup test data
    // Act (When) - Execute method under test
    // Assert (Then) - Verify result
}
```

#### Basic Test Example

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int divide(int a, int b) {
        if (b == 0) {
            throw new IllegalArgumentException("Cannot divide by zero");
        }
        return a / b;
    }
}

class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }
    
    @Test
    void add_twoPositiveNumbers_returnsSum() {
        // Arrange
        int num1 = 5;
        int num2 = 3;
        
        // Act
        int result = calculator.add(num1, num2);
        
        // Assert
        assertEquals(8, result);
    }
    
    @Test
    void divide_validNumbers_returnsQuotient() {
        assertEquals(4, calculator.divide(8, 2));
    }
    
    @Test
    void divide_byZero_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            calculator.divide(10, 0);
        });
    }
}
```

### Service Layer Testing

#### Service Class

```java
@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;
    
    public Product saveProduct(Product product) {
        return repository.save(product);
    }
    
    public Product getProductById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
    }
    
    public List<Product> getAllProducts() {
        return repository.findAll();
    }
    
    public void deleteProduct(Long id) {
        repository.deleteById(id);
    }
}
```

#### Service Test (with Mockito)

```java
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    
    @Mock
    private ProductRepository repository;
    
    @InjectMocks
    private ProductService service;
    
    @Test
    void saveProduct_validProduct_returnsSavedProduct() {
        // Arrange
        Product product = new Product(null, "Laptop", 1200.0);
        Product savedProduct = new Product(1L, "Laptop", 1200.0);
        
        when(repository.save(product)).thenReturn(savedProduct);
        
        // Act
        Product result = service.saveProduct(product);
        
        // Assert
        assertNotNull(result.getId());
        assertEquals("Laptop", result.getName());
        verify(repository, times(1)).save(product);
    }
    
    @Test
    void getProductById_existingId_returnsProduct() {
        // Arrange
        Long productId = 1L;
        Product product = new Product(productId, "Laptop", 1200.0);
        
        when(repository.findById(productId)).thenReturn(Optional.of(product));
        
        // Act
        Product result = service.getProductById(productId);
        
        // Assert
        assertEquals(productId, result.getId());
        assertEquals("Laptop", result.getName());
    }
    
    @Test
    void getProductById_nonExistingId_throwsException() {
        // Arrange
        Long productId = 999L;
        when(repository.findById(productId)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(ProductNotFoundException.class, () -> {
            service.getProductById(productId);
        });
    }
    
    @Test
    void getAllProducts_returnsAllProducts() {
        // Arrange
        List<Product> products = Arrays.asList(
            new Product(1L, "Laptop", 1200.0),
            new Product(2L, "Mouse", 25.0)
        );
        
        when(repository.findAll()).thenReturn(products);
        
        // Act
        List<Product> result = service.getAllProducts();
        
        // Assert
        assertEquals(2, result.size());
        verify(repository).findAll();
    }
}
```

### JUnit 5 Assertions

```java
class AssertionsTest {
    
    @Test
    void testAssertions() {
        // Basic assertions
        assertEquals(4, 2 + 2);
        assertNotEquals(5, 2 + 2);
        assertTrue(5 > 3);
        assertFalse(5 < 3);
        assertNull(null);
        assertNotNull("value");
        
        // String assertions
        String text = "Hello World";
        assertTrue(text.contains("World"));
        assertTrue(text.startsWith("Hello"));
        assertTrue(text.endsWith("World"));
        
        // Array assertions
        int[] expected = {1, 2, 3};
        int[] actual = {1, 2, 3};
        assertArrayEquals(expected, actual);
        
        // Collection assertions
        List<String> list = Arrays.asList("A", "B", "C");
        assertTrue(list.contains("B"));
        assertEquals(3, list.size());
        
        // Exception assertions
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            throw new IllegalArgumentException("Invalid argument");
        });
        assertEquals("Invalid argument", exception.getMessage());
        
        // Grouped assertions
        assertAll("person",
            () -> assertEquals("John", "John"),
            () -> assertEquals(30, 30),
            () -> assertTrue(true)
        );
    }
}
```

### Test Lifecycle Annotations

```java
class LifecycleTest {
    
    @BeforeAll
    static void initAll() {
        // Runs once before all tests
        System.out.println("Before all tests");
    }
    
    @BeforeEach
    void init() {
        // Runs before each test
        System.out.println("Before each test");
    }
    
    @Test
    void test1() {
        System.out.println("Test 1");
    }
    
    @Test
    void test2() {
        System.out.println("Test 2");
    }
    
    @AfterEach
    void tearDown() {
        // Runs after each test
        System.out.println("After each test");
    }
    
    @AfterAll
    static void tearDownAll() {
        // Runs once after all tests
        System.out.println("After all tests");
    }
}
```

---

## Integration Testing

### @SpringBootTest

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductIntegrationTest {
    
    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private ProductService service;
    
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
    
    @Test
    void saveAndRetrieveProduct_success() {
        // Arrange
        Product product = new Product(null, "Laptop", 1200.0);
        
        // Act
        Product saved = service.saveProduct(product);
        Product retrieved = service.getProductById(saved.getId());
        
        // Assert
        assertNotNull(retrieved);
        assertEquals("Laptop", retrieved.getName());
        assertEquals(1200.0, retrieved.getPrice());
    }
    
    @Test
    void getAllProducts_returnsAllSavedProducts() {
        // Arrange
        service.saveProduct(new Product(null, "Laptop", 1200.0));
        service.saveProduct(new Product(null, "Mouse", 25.0));
        
        // Act
        List<Product> products = service.getAllProducts();
        
        // Assert
        assertEquals(2, products.size());
    }
}
```

### @DataJpaTest

```java
@DataJpaTest
class ProductRepositoryTest {
    
    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private TestEntityManager entityManager;
    
    @Test
    void findByName_existingProduct_returnsProduct() {
        // Arrange
        Product product = new Product(null, "Laptop", 1200.0);
        entityManager.persist(product);
        entityManager.flush();
        
        // Act
        Optional<Product> found = repository.findByName("Laptop");
        
        // Assert
        assertTrue(found.isPresent());
        assertEquals("Laptop", found.get().getName());
    }
    
    @Test
    void findByPriceGreaterThan_returnsMatchingProducts() {
        // Arrange
        entityManager.persist(new Product(null, "Laptop", 1200.0));
        entityManager.persist(new Product(null, "Mouse", 25.0));
        entityManager.persist(new Product(null, "Keyboard", 75.0));
        entityManager.flush();
        
        // Act
        List<Product> expensiveProducts = repository.findByPriceGreaterThan(100.0);
        
        // Assert
        assertEquals(1, expensiveProducts.size());
        assertEquals("Laptop", expensiveProducts.get(0).getName());
    }
}
```

### @WebMvcTest

```java
@WebMvcTest(ProductController.class)
class ProductControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ProductService service;
    
    @Test
    void getProduct_existingId_returnsProduct() throws Exception {
        // Arrange
        Long productId = 1L;
        Product product = new Product(productId, "Laptop", 1200.0);
        
        when(service.getProductById(productId)).thenReturn(product);
        
        // Act & Assert
        mockMvc.perform(get("/api/products/{id}", productId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1200.0));
    }
    
    @Test
    void createProduct_validProduct_returnsCreated() throws Exception {
        // Arrange
        Product product = new Product(null, "Laptop", 1200.0);
        Product savedProduct = new Product(1L, "Laptop", 1200.0);
        
        when(service.saveProduct(any(Product.class))).thenReturn(savedProduct);
        
        // Act & Assert
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Laptop\",\"price\":1200.0}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Laptop"));
    }
}
```

---

## REST API Testing

### MockMvc Testing

```java
@SpringBootTest
@AutoConfigureMockMvc
class ProductRestControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ProductRepository repository;
    
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
    
    @Test
    void getAllProducts_returnsProductList() throws Exception {
        // Arrange
        repository.save(new Product(null, "Laptop", 1200.0));
        repository.save(new Product(null, "Mouse", 25.0));
        
        // Act & Assert
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[1].name").value("Mouse"));
    }
    
    @Test
    void createProduct_validProduct_returnsCreated() throws Exception {
        String productJson = """
                {
                    "name": "Keyboard",
                    "price": 75.0
                }
                """;
        
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(productJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("Keyboard"))
                .andExpect(jsonPath("$.price").value(75.0));
        
        assertEquals(1, repository.count());
    }
    
    @Test
    void updateProduct_existingProduct_returnsUpdated() throws Exception {
        Product product = repository.save(new Product(null, "Laptop", 1200.0));
        
        String updatedJson = """
                {
                    "id": %d,
                    "name": "Gaming Laptop",
                    "price": 1500.0
                }
                """.formatted(product.getId());
        
        mockMvc.perform(put("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gaming Laptop"))
                .andExpect(jsonPath("$.price").value(1500.0));
    }
    
    @Test
    void deleteProduct_existingId_returnsNoContent() throws Exception {
        Product product = repository.save(new Product(null, "Laptop", 1200.0));
        
        mockMvc.perform(delete("/api/products/{id}", product.getId()))
                .andExpect(status().isNoContent());
        
        assertEquals(0, repository.count());
    }
    
    @Test
    void getProduct_nonExistingId_returnsNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound());
    }
}
```

### TestRestTemplate

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductRestTemplateTest {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private ProductRepository repository;
    
    private String baseUrl;
    
    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/api/products";
        repository.deleteAll();
    }
    
    @Test
    void getAllProducts_returnsProductList() {
        // Arrange
        repository.save(new Product(null, "Laptop", 1200.0));
        
        // Act
        ResponseEntity<Product[]> response = restTemplate.getForEntity(baseUrl, Product[].class);
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().length);
    }
    
    @Test
    void createProduct_validProduct_returnsCreated() {
        // Arrange
        Product product = new Product(null, "Laptop", 1200.0);
        
        // Act
        ResponseEntity<Product> response = restTemplate.postForEntity(baseUrl, product, Product.class);
        
        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getId());
        assertEquals("Laptop", response.getBody().getName());
    }
}
```

---

## Mocking with Mockito

### Basic Mocking

```java
@ExtendWith(MockitoExtension.class)
class MockitoBasicsTest {
    
    @Mock
    private ProductRepository repository;
    
    @Test
    void basicMocking() {
        // When-Then
        when(repository.findById(1L)).thenReturn(Optional.of(new Product(1L, "Laptop", 1200.0)));
        
        Optional<Product> product = repository.findById(1L);
        
        assertTrue(product.isPresent());
        assertEquals("Laptop", product.get().getName());
    }
}
```

### Argument Matchers

```java
@Test
void argumentMatchers() {
    // any()
    when(repository.save(any(Product.class)))
            .thenReturn(new Product(1L, "Laptop", 1200.0));
    
    // anyLong()
    when(repository.findById(anyLong()))
            .thenReturn(Optional.of(new Product(1L, "Laptop", 1200.0)));
    
    // eq()
    when(repository.findByNameAndPrice(eq("Laptop"), anyDouble()))
            .thenReturn(Optional.of(new Product(1L, "Laptop", 1200.0)));
    
    // Custom matcher
    when(repository.findByPrice(argThat(price -> price > 1000)))
            .thenReturn(Arrays.asList(new Product(1L, "Laptop", 1200.0)));
}
```

### Verify Interactions

```java
@Test
void verifyInteractions() {
    Product product = new Product(null, "Laptop", 1200.0);
    
    service.saveProduct(product);
    
    // Verify method called
    verify(repository).save(product);
    
    // Verify method called with specific arguments
    verify(repository).save(argThat(p -> p.getName().equals("Laptop")));
    
    // Verify number of times
    verify(repository, times(1)).save(product);
    verify(repository, atLeastOnce()).save(product);
    verify(repository, atMost(2)).save(product);
    verify(repository, never()).deleteAll();
    
    // Verify no more interactions
    verifyNoMoreInteractions(repository);
}
```

### Stubbing Void Methods

```java
@Test
void stubbingVoidMethods() {
    Long productId = 1L;
    
    // Do nothing (default behavior)
    doNothing().when(repository).deleteById(productId);
    
    // Throw exception
    doThrow(new RuntimeException("Error")).when(repository).deleteById(999L);
    
    // Test
    service.deleteProduct(productId);
    verify(repository).deleteById(productId);
    
    assertThrows(RuntimeException.class, () -> service.deleteProduct(999L));
}
```

### Answer

```java
@Test
void customAnswer() {
    when(repository.save(any(Product.class))).thenAnswer(invocation -> {
        Product product = invocation.getArgument(0);
        product.setId(1L);  // Simulate ID generation
        return product;
    });
    
    Product product = new Product(null, "Laptop", 1200.0);
    Product saved = service.saveProduct(product);
    
    assertNotNull(saved.getId());
}
```

### @Spy

```java
@Spy
private List<String> spyList = new ArrayList<>();

@Test
void spyExample() {
    // Real methods are called
    spyList.add("one");
    spyList.add("two");
    
    // Verify real method called
    verify(spyList).add("one");
    assertEquals(2, spyList.size());
    
    // Stub specific method
    when(spyList.size()).thenReturn(100);
    assertEquals(100, spyList.size());
}
```

---

## Test Coverage

### JaCoCo Plugin

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.jacoco</groupId>
            <artifactId>jacoco-maven-plugin</artifactId>
            <version>0.8.8</version>
            <executions>
                <execution>
                    <goals>
                        <goal>prepare-agent</goal>
                    </goals>
                </execution>
                <execution>
                    <id>report</id>
                    <phase>test</phase>
                    <goals>
                        <goal>report</goal>
                    </goals>
                </execution>
                <execution>
                    <id>check</id>
                    <goals>
                        <goal>check</goal>
                    </goals>
                    <configuration>
                        <rules>
                            <rule>
                                <element>PACKAGE</element>
                                <limits>
                                    <limit>
                                        <counter>LINE</counter>
                                        <value>COVEREDRATIO</value>
                                        <minimum>0.80</minimum>
                                    </limit>
                                </limits>
                            </rule>
                        </rules>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

### Generate Coverage Report

```bash
mvn clean test
mvn jacoco:report
```

**Report Location**: `target/site/jacoco/index.html`

### Testing Best Practices

1. **Follow AAA Pattern**
   - Arrange: Setup test data
   - Act: Execute method
   - Assert: Verify results

2. **One Assertion Per Test** (when possible)
   - Easier to identify failures
   - Better test names

3. **Use Descriptive Test Names**
   ```java
   @Test
   void getProduct_whenIdDoesNotExist_throwsProductNotFoundException()
   ```

4. **Test Edge Cases**
   - Null values
   - Empty collections
   - Boundary values
   - Exceptional scenarios

5. **Keep Tests Independent**
   - No shared state
   - Can run in any order

6. **Mock External Dependencies**
   - Databases
   - HTTP calls
   - File systems

7. **Aim for High Coverage**
   - Minimum 80% line coverage
   - Focus on critical business logic

8. **Use @BeforeEach for Setup**
   - Common initialization
   - Clean state for each test

---

## Complete Test Example

```java
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductServiceIntegrationTest {
    
    @Autowired
    private ProductService service;
    
    @Autowired
    private ProductRepository repository;
    
    @Autowired
    private MockMvc mockMvc;
    
    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }
    
    @Nested
    @DisplayName("Product Creation Tests")
    class ProductCreationTests {
        
        @Test
        @DisplayName("Should create product with valid data")
        void createProduct_validData_success() {
            // Arrange
            Product product = new Product(null, "Laptop", 1200.0);
            
            // Act
            Product saved = service.saveProduct(product);
            
            // Assert
            assertAll(
                () -> assertNotNull(saved.getId()),
                () -> assertEquals("Laptop", saved.getName()),
                () -> assertEquals(1200.0, saved.getPrice())
            );
        }
        
        @Test
        @DisplayName("Should throw exception for null product")
        void createProduct_nullProduct_throwsException() {
            assertThrows(IllegalArgumentException.class, () -> {
                service.saveProduct(null);
            });
        }
    }
    
    @Nested
    @DisplayName("Product Retrieval Tests")
    class ProductRetrievalTests {
        
        @Test
        @DisplayName("Should retrieve product by existing ID")
        void getProduct_existingId_returnsProduct() {
            Product saved = repository.save(new Product(null, "Laptop", 1200.0));
            
            Product retrieved = service.getProductById(saved.getId());
            
            assertNotNull(retrieved);
            assertEquals(saved.getId(), retrieved.getId());
        }
        
        @Test
        @DisplayName("Should throw exception for non-existing ID")
        void getProduct_nonExistingId_throwsException() {
            assertThrows(ProductNotFoundException.class, () -> {
                service.getProductById(999L);
            });
        }
    }
    
    @Nested
    @DisplayName("REST API Tests")
    class RestApiTests {
        
        @Test
        @DisplayName("GET /api/products should return all products")
        void getAllProducts_returnsProductList() throws Exception {
            repository.save(new Product(null, "Laptop", 1200.0));
            repository.save(new Product(null, "Mouse", 25.0));
            
            mockMvc.perform(get("/api/products"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$", hasSize(2)))
                    .andDo(print());
        }
        
        @Test
        @DisplayName("POST /api/products should create new product")
        void createProduct_validRequest_returnsCreated() throws Exception {
            String productJson = """
                    {
                        "name": "Keyboard",
                        "price": 75.0
                    }
                    """;
            
            mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(productJson))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.id").exists())
                    .andExpect(jsonPath("$.name").value("Keyboard"))
                    .andDo(print());
        }
    }
}
```

---

*End of Spring Boot Testing Guide*

**Next Steps**:
- Practice writing tests for your applications
- Aim for high test coverage
- Use CI/CD pipelines to run tests automatically
- Review test reports regularly
