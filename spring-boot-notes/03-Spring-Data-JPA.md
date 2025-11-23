# Spring Data JPA

## Table of Contents
- [Introduction](#introduction)
- [Repository Interfaces](#repository-interfaces)
- [CrudRepository Methods](#crudrepository-methods)
- [Custom Queries](#custom-queries)
- [Pagination and Sorting](#pagination-and-sorting)
- [Primary Key Generators](#primary-key-generators)
- [Composite Primary Keys](#composite-primary-keys)
- [Timestamps](#timestamps)

---

## Introduction

Spring Data JPA simplifies database operations by reducing boilerplate code.

### Technology Stack

```
Java App → Spring Data JPA → Hibernate ORM → JDBC → Database
```

### Why Spring Data JPA?

**Problem**: For 5000 DB tables, we need:
- 5000 DAO classes
- 20,000 methods (4 CRUD methods per class)
- Same logic repeated everywhere

**Solution**: Spring Data JPA provides:
- 0 methods to write for basic CRUD
- Uses Hibernate framework internally
- Automatic query generation

### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Database Driver -->
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
```

### Auto-Configuration

Spring Data JPA automatically:
- Loads database driver
- Creates database connection
- Creates SessionFactory
- Manages transactions

---

## Repository Interfaces

### Repository Hierarchy

```
Repository (I)
   ↑
   └── CrudRepository (I)        --> (12 methods)
           ↑
           └── PagingAndSortingRepository (I)
                   ↑
                   └── JpaRepository (I)
                           ↑
                           └── QueryByExampleExecutor (I)
```

### Interface Comparison

| Interface | Extends From | Purpose |
|-----------|-------------|---------|
| **Repository** | — | Marker interface (root) |
| **CrudRepository** | Repository | CRUD operations (≈12 methods) |
| **PagingAndSortingRepository** | CrudRepository | Pagination and sorting support |
| **JpaRepository** | PagingAndSortingRepository, QueryByExampleExecutor | JPA-specific methods + batch operations |
| **QueryByExampleExecutor** | — | Query by Example (QBE) functionality |

### Creating a Repository

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    // No methods needed for basic CRUD!
}
```

**Type Parameters**:
- First: Entity class (`Book`)
- Second: Primary key type (`Integer`)

---

## CrudRepository Methods

### Complete Method List

| Method | Signature | Description | Example |
|--------|-----------|-------------|---------|
| **save** | `<S extends T> S save(S entity)` | Inserts if new, updates if ID exists | `repo.save(emp);` |
| **saveAll** | `<S extends T> Iterable<S> saveAll(Iterable<S> entities)` | Batch save/update | `repo.saveAll(listOfEmployees);` |
| **findById** | `Optional<T> findById(ID id)` | Retrieve entity by ID | `repo.findById(1L);` |
| **existsById** | `boolean existsById(ID id)` | Check if entity exists | `repo.existsById(5L);` |
| **findAll** | `Iterable<T> findAll()` | Retrieve all entities | `repo.findAll();` |
| **findAllById** | `Iterable<T> findAllById(Iterable<ID> ids)` | Retrieve multiple by IDs | `repo.findAllById(List.of(1L,2L));` |
| **count** | `long count()` | Total number of entities | `repo.count();` |
| **deleteById** | `void deleteById(ID id)` | Delete by ID | `repo.deleteById(3L);` |
| **delete** | `void delete(T entity)` | Delete given entity | `repo.delete(emp);` |
| **deleteAllById** | `void deleteAllById(Iterable<? extends ID> ids)` | Delete multiple by IDs | `repo.deleteAllById(List.of(1L,2L));` |
| **deleteAll(entities)** | `void deleteAll(Iterable<? extends T> entities)` | Delete given entities | `repo.deleteAll(listOfEmployees);` |
| **deleteAll()** | `void deleteAll()` | **Delete ALL** (use with caution!) | `repo.deleteAll();` |

### Important Notes

1. **save()** - Polymorphic method:
   - If ID is null or not in DB → INSERT
   - If ID exists in DB → UPDATE (executes SELECT first, then INSERT/UPDATE)

2. **findById()** returns `Optional<T>`:
   - Helps avoid NullPointerException
   ```java
   Optional<Book> optionalBook = repo.findById(1);
   if (optionalBook.isPresent()) {
       Book book = optionalBook.get();
   }
   ```

3. **findAll()** execution:
   - Executes SELECT query
   - Retrieves records as ResultSet
   - Moves cursor and creates Entity objects
   - Returns Collection

---

## Custom Queries

### Query Methods Overview

| Type | Annotation | Description | Example |
|------|------------|-------------|---------|
| **Derived Query** | — | Auto-generated from method name | `findByName(String name)` |
| **JPQL Query** | `@Query` | Uses entity and field names | `@Query("SELECT e FROM Employee e WHERE e.id=:id")` |
| **Native Query** | `@Query(..., nativeQuery=true)` | Uses table and column names | `@Query("SELECT * FROM employee", nativeQuery=true)` |
| **Modifying Query** | `@Modifying` + `@Transactional` | For UPDATE/DELETE/INSERT | `@Query("DELETE FROM Employee e WHERE e.id=:id")` |

### Derived Query Methods

Spring Data JPA generates queries based on method names:

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // Find by single property
    List<Book> findByName(String name);
    
    // Find by price greater than
    List<Book> findByBookPriceGreaterThan(Double price);
    
    // Find by multiple properties
    List<Book> findByNameAndAuthor(String name, String author);
    
    // Find with sorting
    List<Book> findByNameOrderByPriceDesc(String name);
    
    // Find with like
    List<Book> findByNameContaining(String keyword);
    
    // Find with between
    List<Book> findByPriceBetween(Double min, Double max);
}
```

#### Keyword Reference

| Method | Generated SQL | Description |
|--------|--------------|-------------|
| `findByName(String name)` | `WHERE name = ?` | Exact match |
| `findByRole(String role)` | `WHERE role = ?` | Exact match |
| `findByPriceGreaterThan(Double price)` | `WHERE price > ?` | Greater than |
| `findByPriceLessThan(Double price)` | `WHERE price < ?` | Less than |
| `findByNameContaining(String keyword)` | `WHERE name LIKE %?%` | Contains |
| `findByNameStartingWith(String prefix)` | `WHERE name LIKE ?%` | Starts with |
| `findByNameEndingWith(String suffix)` | `WHERE name LIKE %?` | Ends with |
| `findByActiveTrue()` | `WHERE active = true` | Boolean true |
| `findByActiveFalse()` | `WHERE active = false` | Boolean false |
| `findByNameIn(List<String> names)` | `WHERE name IN (?)` | In list |
| `findByPriceBetween(Double min, Double max)` | `WHERE price BETWEEN ? AND ?` | Between range |

**Important**: Method names must use Entity class property names, not database column names!

### JPQL (Hibernate Query Language)

HQL works on entity classes and their properties, not database tables.

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // HQL Query
    @Query("FROM Book WHERE bookPrice > :price")
    List<Book> findExpensiveBooks(@Param("price") Double price);
    
    // HQL with JOIN
    @Query("SELECT b FROM Book b JOIN b.author a WHERE a.name = :name")
    List<Book> findBooksByAuthorName(@Param("name") String name);
    
    // Projection (specific columns)
    @Query("SELECT b.name, b.price FROM Book b")
    List<Object[]> getBookNamesAndPrices();
    
    // Count query
    @Query("SELECT COUNT(b) FROM Book b WHERE b.price > :price")
    Long countExpensiveBooks(@Param("price") Double price);
}
```

### Native SQL Queries

Use actual table and column names:

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // Native SQL Query
    @Query(value = "SELECT * FROM book WHERE book_price > :price", 
           nativeQuery = true)
    List<Book> findExpensiveBooksNative(@Param("price") Double price);
    
    // Native query with JOIN
    @Query(value = "SELECT b.* FROM book b INNER JOIN author a " +
                   "ON b.author_id = a.id WHERE a.name = :name", 
           nativeQuery = true)
    List<Book> findByAuthorNameNative(@Param("name") String name);
}
```

### HQL vs SQL Comparison

| Feature | HQL | SQL |
|---------|-----|-----|
| **Works On** | Entity classes and properties | Database tables and columns |
| **Database Dependency** | Independent (portable) | Dependent (vendor-specific) |
| **Return Type** | Entity objects | ResultSet (raw data) |
| **Case Sensitivity** | Entity/field names are case-sensitive | Depends on DB |
| **Joins** | Uses entity relationships | Explicit JOIN syntax |
| **Example** | `FROM Book WHERE bookPrice > 200` | `SELECT * FROM book WHERE book_price > 200` |

**Note**: Dialect class converts HQL to SQL (e.g., MySQLDialect, OracleDialect, PostgresDialect)

### Modifying Queries (UPDATE/DELETE)

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    @Modifying
    @Transactional
    @Query("UPDATE Book b SET b.price = :price WHERE b.id = :id")
    int updateBookPrice(@Param("id") Integer id, @Param("price") Double price);
    
    @Modifying
    @Transactional
    @Query("DELETE FROM Book b WHERE b.price < :price")
    int deleteBooksBelowPrice(@Param("price") Double price);
}
```

**Important**: 
- `@Modifying` is required for UPDATE/DELETE
- `@Transactional` is required for data modification
- Direct INSERT is not supported (use `save()` instead)

---

## Pagination and Sorting

### Sorting

```java
// Ascending order by name
Sort sortByNameAsc = Sort.by("name").ascending();
List<Book> books = bookRepository.findAll(sortByNameAsc);

// Descending order by price
Sort sortByPriceDesc = Sort.by("price").descending();
List<Book> books = bookRepository.findAll(sortByPriceDesc);

// Multiple fields
Sort complexSort = Sort.by("category").ascending()
                       .and(Sort.by("price").descending());
List<Book> books = bookRepository.findAll(complexSort);
```

### Pagination

```java
// Page 0 (first page), 10 items per page
Pageable pageable = PageRequest.of(0, 10);
Page<Book> page = bookRepository.findAll(pageable);

// Get content
List<Book> books = page.getContent();
int totalPages = page.getTotalPages();
long totalElements = page.getTotalElements();
boolean hasNext = page.hasNext();
```

### Pagination with Sorting

```java
// Page 0, 10 items, sorted by name ascending
Pageable pageable = PageRequest.of(0, 10, Sort.by("name").ascending());
Page<Book> page = bookRepository.findAll(pageable);
```

### Custom Pagination Methods

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // Paginated query
    Page<Book> findByPriceGreaterThan(Double price, Pageable pageable);
    
    // Sorted query
    List<Book> findByCategory(String category, Sort sort);
}

// Usage
Pageable pageable = PageRequest.of(0, 20, Sort.by("price").descending());
Page<Book> expensiveBooks = bookRepository.findByPriceGreaterThan(100.0, pageable);
```

### Query By Example (QBE)

```java
// Create example (probe) object
Book probe = new Book();
probe.setCategory("Fiction");
probe.setPublisher("Penguin");

// Create Example
Example<Book> example = Example.of(probe);

// Find matching records
List<Book> books = bookRepository.findAll(example);
```

#### With ExampleMatcher

```java
ExampleMatcher matcher = ExampleMatcher.matching()
    .withIgnoreCase("name")
    .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);

Book probe = new Book();
probe.setName("java");  // Will match "Java", "JAVA", "Advanced Java"

Example<Book> example = Example.of(probe, matcher);
List<Book> books = bookRepository.findAll(example);
```

---

## Primary Key Generators

### IdentifierGenerationException

Occurs when Hibernate fails to generate a primary key. Common causes:

| Cause | Solution |
|-------|----------|
| Missing `@GeneratedValue` | Add generation strategy |
| Unsupported strategy for DB | Use appropriate strategy (IDENTITY for MySQL, SEQUENCE for Oracle) |
| Sequence/table not found | Create in DB or use `ddl-auto=update` |
| Manually assigned ID not set | Set ID manually or use `@GeneratedValue` |

### JPA Generation Strategies

| Strategy | Database Support | Performance | Batch Insert | Example |
|----------|-----------------|-------------|--------------|---------|
| **AUTO** (Default) | All databases | Varies | Varies | `@GeneratedValue(strategy = GenerationType.AUTO)` |
| **IDENTITY** | MySQL, SQL Server, PostgreSQL, H2 | Good | ❌ | `@GeneratedValue(strategy = GenerationType.IDENTITY)` |
| **SEQUENCE** | Oracle, PostgreSQL, H2, DB2 | Excellent | ✅ | `@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")` |
| **TABLE** | All databases | Fair | ✅ | `@GeneratedValue(strategy = GenerationType.TABLE, generator = "tab")` |

### Examples

#### IDENTITY (Auto-increment)

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private Double price;
}
```

**SQL Generated**:
```sql
CREATE TABLE book (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255),
    price DOUBLE
);
```

#### SEQUENCE

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_sequence", 
                       allocationSize = 1)
    private Integer id;
    
    private String name;
    private Double price;
}
```

#### TABLE

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "book_gen")
    @TableGenerator(name = "book_gen", table = "id_generator", 
                    pkColumnName = "gen_name", valueColumnName = "gen_value",
                    pkColumnValue = "book_id", allocationSize = 1)
    private Integer id;
    
    private String name;
    private Double price;
}
```

### Custom Generator

```java
public class OrderIdGenerator implements IdentifierGenerator {
    
    @Override
    public Serializable generate(SharedSessionContractImplementor session, 
                                Object object) {
        String prefix = "OD";
        String suffix = "";
        
        try {
            Connection connection = session.getJdbcConnectionAccess().obtainConnection();
            Statement statement = connection.createStatement();
            String query = "SELECT ORDER_ID_SEQ.NEXTVAL as nextval FROM DUAL";
            ResultSet result = statement.executeQuery(query);
            
            if (result.next()) {
                int seqVal = result.getInt(1);
                suffix = String.valueOf(seqVal);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return prefix + suffix;  // Returns OD1, OD2, OD3, etc.
    }
}

@Entity
public class Order {
    @Id
    @GeneratedValue(generator = "order_id_gen")
    @GenericGenerator(name = "order_id_gen", 
                     strategy = "com.example.OrderIdGenerator")
    private String orderId;
}
```

---

## Composite Primary Keys

When a table has multiple columns as primary key.

### Using @EmbeddedId

```java
@Data
@Embeddable
public class AccountPK implements Serializable {
    private String accNum;
    private String accType;
}

@Entity
@Table(name = "account_tbl")
@Data
public class Account {
    private String holderName;
    private String branch;
    
    @EmbeddedId
    private AccountPK accountPK;
}

public interface AccountRepository extends JpaRepository<Account, AccountPK> {
}
```

### Usage

```java
// Create composite key
AccountPK pk = new AccountPK();
pk.setAccNum("ACC001");
pk.setAccType("SAVINGS");

// Create account
Account account = new Account();
account.setAccountPK(pk);
account.setHolderName("John Doe");
account.setBranch("Mumbai");

accountRepository.save(account);

// Find by composite key
Optional<Account> account = accountRepository.findById(pk);
```

**Important Notes**:
1. Composite key class must implement `Serializable`
2. Generators are NOT applicable for composite keys
3. Values must be set manually

---

## Timestamps

Auto-populate creation and update timestamps:

```java
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private Double price;
    
    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDateTime dateCreated;
    
    @UpdateTimestamp
    @Column(name = "last_updated", insertable = false)
    private LocalDateTime lastUpdated;
}
```

**Behavior**:
- `@CreationTimestamp`: Set once during INSERT, never updated
- `@UpdateTimestamp`: Updated every time record is modified

---

## Selection vs Projection

| Term | Meaning | SQL Clause | Example |
|------|---------|------------|---------|
| **Selection** | Filters specific **rows** | `WHERE` | `SELECT * FROM emp WHERE dept='HR'` |
| **Projection** | Selects specific **columns** | `SELECT` | `SELECT name, salary FROM emp` |

### Projection Example

```java
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    // Projection - only specific columns
    @Query("SELECT b.name, b.price FROM Book b")
    List<Object[]> getBookNamesAndPrices();
    
    // Using interface projection
    interface BookSummary {
        String getName();
        Double getPrice();
    }
    
    @Query("SELECT b.name as name, b.price as price FROM Book b")
    List<BookSummary> getBookSummaries();
}
```

---

*Continue to [04-Spring-Web-MVC.md](04-Spring-Web-MVC.md) for web application development.*
