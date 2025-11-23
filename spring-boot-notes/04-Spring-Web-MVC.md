# Spring Web MVC

## Table of Contents
- [MVC Architecture](#mvc-architecture)
- [Controllers](#controllers)
- [Form Handling](#form-handling)
- [Form Tag Library](#form-tag-library)
- [Thymeleaf](#thymeleaf)
- [Form Validation](#form-validation)
- [Exception Handling](#exception-handling)

---

## MVC Architecture

### Overview

Spring WebMVC simplifies web application development with features like:
- Form binding (Form Data → Java Object)
- Type conversion
- Multiple presentation technologies (JSP, Thymeleaf)
- Form tag library

### MVC Flow Diagram

```
Client Request → DispatcherServlet → Controller → Service → DAO → Database
       ↑                                              ↓
Client Response ← View ← Model ← Controller ← Service ←
```

### Detailed Flow

```
CLIENT REQUEST
    ↓
DispatcherServlet (Front Controller)
    ↓
Handler Mapping (Finds appropriate controller)
    ↓
Controller (Processes request)
    ↓
Service Layer (Business logic)
    ↓
DAO/Repository (Data access)
    ↓
Database
    ↑
Controller ← Service ← DAO (Return data)
    ↓
Model (Adds data)
    ↓
View Resolver (Finds view template)
    ↓
View (JSP/Thymeleaf) + Model
    ↓
CLIENT RESPONSE
```

### Key Components

| Component | Responsibility |
|-----------|---------------|
| **DispatcherServlet** | Front controller, handles all requests |
| **Handler Mapping** | Maps URL to controller method |
| **Controller** | Processes business logic |
| **Service** | Business logic implementation |
| **DAO/Repository** | Data access operations |
| **Model** | Data in key-value format |
| **View Resolver** | Identifies view file and technology |
| **View** | Presentation layer (JSP/Thymeleaf) |

---

## Controllers

### Dependencies

```xml
<dependencies>
    <!-- Spring Web MVC -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <!-- DevTools (optional) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-devtools</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- For JSP (if using JSP) -->
    <dependency>
        <groupId>org.apache.tomcat.embed</groupId>
        <artifactId>tomcat-embed-jasper</artifactId>
    </dependency>
    
    <!-- JSTL (for JSP) -->
    <dependency>
        <groupId>javax.servlet</groupId>
        <artifactId>jstl</artifactId>
    </dependency>
</dependencies>
```

### Basic Controller

```java
@Controller
public class ProductController {
    
    @GetMapping("/")
    public String loadForm(Model model) {
        model.addAttribute("p", new Product());
        return "index";  // Returns view name
    }
    
    @PostMapping("/product")
    public String handleSave(@ModelAttribute("p") Product product, Model model) {
        // Save product logic
        model.addAttribute("msg", "Product saved successfully!");
        return "index";
    }
}
```

### @Controller vs @RestController

```java
@Controller
@RequestMapping("/api")
public class MsgController {
    
    @GetMapping("/welcome")
    public String welcomeMsg(Model model) {
        model.addAttribute("msg", "Welcome to the team");
        return "index";  // Returns view name
    }
    
    @GetMapping("/greet")
    @ResponseBody  // Returns data directly, not view name
    public String greetMsg() {
        return "Greetings";  // Returns string as HTTP response
    }
}
```

**Note**: `@Controller` + `@ResponseBody` = Method becomes REST method

### Request Mapping

```java
@Controller
@RequestMapping("/products")
public class ProductController {
    
    // Multiple URL patterns for single method
    @GetMapping(value = {"/", "/list", "/all"})
    public String listProducts(Model model) {
        // Logic
        return "product-list";
    }
    
    @GetMapping("/{id}")
    public String getProduct(@PathVariable("id") Integer id, Model model) {
        // Logic
        return "product-detail";
    }
}
```

### Context Path Configuration

**application.properties**:
```properties
server.port=9090
server.servlet.context-path=/myapp
```

**URL**: `http://localhost:9090/myapp/products`

---

## Form Handling

### Model Attribute

```java
@Controller
public class StudentController {
    
    @Autowired
    private StudentService service;
    
    @GetMapping("/")
    public String loadForm(Model model) {
        init(model);
        return "index";
    }
    
    // Common initialization method
    @ModelAttribute
    public void init(Model model) {
        model.addAttribute("student", new Student());
        model.addAttribute("courses", service.getCourses());
        model.addAttribute("prefTimings", service.getTimings());
    }
    
    @PostMapping("/save")
    public String handleSubmit(Student student, Model model) {
        boolean isSaved = service.saveStudent(student);
        if (isSaved) {
            model.addAttribute("msg", "Data Saved Successfully!");
        }
        init(model);
        return "index";
    }
}
```

### Binding Object (Command Object)

```java
@Data
public class Student {
    private Integer id;
    private String name;
    private String email;
    private String course;
    private String timing;
}
```

**Note**: Every HTTP request creates a new Model object. Use `@ModelAttribute` to avoid code duplication.

---

## Form Tag Library

### JSP Tag Library

Add to JSP file:
```jsp
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
```

### Common Form Tags

| Tag | Description | Example |
|-----|-------------|---------|
| `<form:form>` | Main form container | `<form:form method="POST" modelAttribute="product">` |
| `<form:input>` | Text input field | `<form:input path="name"/>` |
| `<form:password>` | Password field | `<form:password path="password"/>` |
| `<form:textarea>` | Multi-line text | `<form:textarea path="description" rows="5"/>` |
| `<form:select>` | Dropdown | `<form:select path="category">` |
| `<form:option>` | Single option | `<form:option value="FICTION">Fiction</form:option>` |
| `<form:options>` | Multiple options from collection | `<form:options items="${categories}"/>` |
| `<form:radiobutton>` | Radio button | `<form:radiobutton path="type" value="EBOOK"/>` |
| `<form:checkbox>` | Checkbox | `<form:checkbox path="features" value="HARDBACK"/>` |
| `<form:errors>` | Display validation errors | `<form:errors path="name" cssClass="error"/>` |
| `<form:hidden>` | Hidden field | `<form:hidden path="id"/>` |
| `<form:label>` | Field label | `<form:label path="name">Book Name</form:label>` |

### JSP Example

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Form</title>
</head>
<body>
    <h2>Product Information</h2>
    
    <p><font color="green">${msg}</font></p>
    
    <form:form action="product" modelAttribute="p" method="POST">
        <input type="hidden" th:field="*{pid}" />
        
        <table>
            <tr>
                <td>Name:</td>
                <td><form:input path="name"/></td>
            </tr>
            <tr>
                <td>Price:</td>
                <td><form:input path="price"/></td>
            </tr>
            <tr>
                <td>Quantity:</td>
                <td><form:input path="qty"/></td>
            </tr>
            <tr>
                <td><a href="/">Reset</a></td>
                <td><input type="submit" value="Save"></td>
            </tr>
        </table>
    </form:form>
    
    <a href="products">View Products</a>
</body>
</html>
```

### View All Products (JSP)

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Products Page</title>
</head>
<body>
    <h2>Product List</h2>
    
    <table border="1">
        <thead>
            <tr>
                <th>S.NO</th>
                <th>Name</th>
                <th>Price</th>
                <th>Quantity</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <c:forEach items="${products}" var="product" varStatus="status">
                <tr>
                    <td>${status.count}</td>
                    <td>${product.name}</td>
                    <td>${product.price}</td>
                    <td>${product.qty}</td>
                    <td>
                        <a href="edit?pid=${product.pid}">Edit</a>
                        <a href="delete?pid=${product.pid}" 
                           onclick="return confirm('Are you sure?')">Delete</a>
                    </td>
                </tr>
            </c:forEach>
        </tbody>
    </table>
</body>
</html>
```

### JSTL forEach Breakdown

| Part | Meaning | Description |
|------|---------|-------------|
| `<c:forEach` | JSTL Loop Tag | Starts the loop iteration |
| `items="${products}"` | Collection to iterate | Refers to products from controller |
| `var="product"` | Loop variable | Each item assigned to this variable |
| `varStatus="index"` | Loop status | Provides counter, index, etc. |

---

## Thymeleaf

### Why Thymeleaf?

| Feature | JSP | Thymeleaf |
|---------|-----|-----------|
| **Natural Templates** | ❌ No | ✅ Works as static HTML |
| **Spring Integration** | ⚠️ Requires configuration | ✅ Excellent native support |
| **Syntax** | ❌ Mixes Java with HTML | ✅ HTML5-compliant |
| **Learning Curve** | ❌ Steeper | ✅ Easy for frontend developers |
| **Performance** | ✅ Good | ✅ Good (compiled templates) |

### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
```

**Note**: No need for `tomcat-embed-jasper` or view resolver configuration!

### Thymeleaf Template Location

```
src/main/resources/
  └── templates/
      ├── index.html
      └── products.html
```

### Thymeleaf Syntax

| JSP | Thymeleaf |
|-----|-----------|
| `<form:form modelAttribute="p">` | `<form th:object="${p}">` |
| `<form:input path="name"/>` | `<input type="text" th:field="*{name}">` |
| `${msg}` | `th:text="${msg}"` |
| `<c:forEach items="${list}" var="item">` | `th:each="item : ${list}"` |
| `${item.name}` | `th:text="${item.name}"` |

### Thymeleaf Example: Form

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Home Page</title>
</head>
<body>
    <div class="container">
        <h3>
            <p th:text="${msg}" class="text-success"></p>
        </h3>
    </div>

    <form th:action="@{/product}" th:object="${p}" method="post">
        <input type="hidden" th:field="*{pid}" />
        
        <table>
            <tr>
                <td>Name:</td>
                <td><input type="text" th:field="*{name}"></td>
            </tr>
            <tr>
                <td>Price:</td>
                <td><input type="number" th:field="*{price}"></td>
            </tr>
            <tr>
                <td>Quantity:</td>
                <td><input type="number" th:field="*{quantity}"></td>
            </tr>
            <tr>
                <td><a href="/">Reset</a></td>
                <td><input type="submit" value="Save" class="btn btn-primary" /></td>
            </tr>
        </table>
    </form>

    <div class="pt-3 pb-3">
        <a href="/products">View All Products</a>
    </div>
</body>
</html>
```

### Thymeleaf Example: List

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Product Page</title>
    <script type="text/javascript">
        function deleteConfirm() {
            return confirm("Are you sure you want to delete this product?");
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>View Products</h1>
        <p th:text="${msg}" class="text-danger"></p>
        <a href="/" class="btn btn-primary">+ Add New Product</a>
    </div>
    
    <table border="1">
        <thead>
            <tr>
                <th>Product ID</th>
                <th>Product Name</th>
                <th>Product Price</th>
                <th>Product Quantity</th>
                <th>Action</th>
            </tr>
        </thead>
        <tbody>
            <tr th:if="${list == null or list.empty}">
                <td colspan="5" style="text-align: center;">No Records found</td>
            </tr>
            <tr th:each="product : ${list}" th:if="${list != null and !list.empty}">
                <td th:text="${product.pid}"></td>
                <td th:text="${product.name}"></td>
                <td th:text="${product.price}"></td>
                <td th:text="${product.quantity}"></td>
                <td>
                    <a th:href="@{/edit(pid=${product.pid})}" class="btn btn-warning">Edit</a>
                    <a th:href="@{/delete(pid=${product.pid})}" class="btn btn-danger"
                       onclick="return deleteConfirm()">Delete</a>
                </td>
            </tr>
        </tbody>
    </table>
</body>
</html>
```

### Key Thymeleaf Attributes

| Attribute | Purpose | Example |
|-----------|---------|---------|
| `th:text` | Set text content | `<p th:text="${msg}"></p>` |
| `th:field` | Bind form field | `<input th:field="*{name}">` |
| `th:object` | Bind form to object | `<form th:object="${product}">` |
| `th:action` | Form action URL | `<form th:action="@{/save}">` |
| `th:href` | Link URL | `<a th:href="@{/products}">Link</a>` |
| `th:each` | Loop iteration | `<tr th:each="item : ${list}">` |
| `th:if` | Conditional display | `<div th:if="${condition}">` |
| `th:unless` | Negative conditional | `<div th:unless="${condition}">` |

---

## Advanced Thymeleaf Features

### Template Modes

Thymeleaf supports six template modes:

| Mode | Type | Description | Usage |
|------|------|-------------|-------|
| **HTML** | Markup | HTML5, HTML4, XHTML - No validation, respects structure | Default for web pages |
| **XML** | Markup | XML documents with validation | XML processing |
| **TEXT** | Textual | Plain text with special syntax | Email templates, text files |
| **JAVASCRIPT** | Textual | JavaScript files with model data & specialized escaping | Dynamic JS with model data |
| **CSS** | Textual | CSS files with special syntax | Dynamic stylesheets |
| **RAW** | No-op | No processing at all | Pass-through content |

**Template Mode Types**:
- **Markup modes**: HTML and XML - Use standard HTML/XML structure
- **Textual modes**: TEXT, JAVASCRIPT, CSS - Use special syntax for processing
- **No-op mode**: RAW - No processing

**Key Points**:
- HTML mode allows any HTML input and doesn't perform validation
- JAVASCRIPT and CSS modes use the same special syntax as TEXT mode
- Textual modes enable model data usage inside JavaScript/CSS files
- HTML mode respects template code/structure in output

### Natural Templating

Thymeleaf templates are valid HTML that can be displayed in browsers without processing:

```html
<!-- JSP (not displayable) -->
<form:inputText name="userName" value="${user.name}" />

<!-- Thymeleaf (displayable - Natural Templating) -->
<input type="text" name="userName" value="James Carrot" th:value="${user.name}" />
```

**Why Natural Templating Matters**:
- Browsers correctly display HTML templates before processing (they ignore unknown attributes like `th:*`)
- Designers can work directly with templates without running the server
- Templates can be opened in browsers for design preview
- Static prototypes become dynamic templates seamlessly

**How It Works**:
- Standard HTML attributes show static content: `value="James Carrot"`
- Thymeleaf attributes override at runtime: `th:value="${user.name}"`
- Browsers ignore `th:*` attributes they don't understand
- Result: Templates are valid HTML files viewable directly

**SpringStandard Dialect**:
- Thymeleaf's official Spring integration packages (`thymeleaf-spring3`, `thymeleaf-spring4`) define the "SpringStandard Dialect"
- Mostly same as Standard Dialect with Spring-specific adaptations
- Uses Spring Expression Language (SpEL) instead of OGNL
- Seamlessly integrates with Spring MVC features

**Standard Dialect Processors**:
- Most processors are **attribute processors**
- Allow browsers to display templates correctly before processing
- Additional attributes don't break HTML structure

### Using Texts and Externalization

**Externalization** is the process of extracting text fragments from template files into separate files (typically `.properties` files) for:
- Easy replacement with equivalent texts in other languages (Internationalization/i18n)
- Centralized message management
- Language-specific content without changing templates

Externalized text fragments are called **messages**, identified by keys.

#### Message Properties (i18n)

**messages.properties** (Default - English):
```properties
welcome.message=Welcome to our application!
user.greeting=Hello, {0}!
home.welcome=Welcome to our <b>fantastic</b> grocery store!
email.placeholder=Enter your email address
subscribe.submit=Subscribe Now!
```

**messages_es.properties** (Spanish):
```properties
welcome.message=¡Bienvenido a nuestra aplicación!
user.greeting=¡Hola, {0}!
home.welcome=¡Bienvenido a nuestra <b>fantástica</b> tienda!
```

**messages_fr.properties** (French):
```properties
welcome.message=Bienvenue dans notre application!
user.greeting=Bonjour, {0}!
```

#### HTML Usage

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Good Thymes Virtual Grocery</title>
    <link rel="stylesheet" th:href="@{/css/gtvg.css}" />
</head>
<body>
    <!-- Message expression: #{...} -->
    <p th:text="#{welcome.message}">Welcome to our grocery store!</p>
    
    <!-- Message with parameters -->
    <p th:text="#{user.greeting(${user.name})}">Hello, User!</p>
    
    <!-- Unescaped HTML content (th:utext) -->
    <p th:utext="#{home.welcome}">Welcome to our store!</p>
    
    <!-- Variable expression with today's date -->
    <p>Today is: <span th:text="${today}">13 February 2011</span></p>
</body>
</html>
```

**Two Features Demonstrated**:
1. **`th:text` attribute**: Evaluates expression and sets result as tag body content
   - Replaces static text like "Welcome to our grocery store!"
   - HTML-escapes the content for security

2. **`#{home.welcome}` expression**: Standard Expression Syntax for messages
   - Instructs `th:text` to use the message with key `home.welcome`
   - Corresponds to locale being processed
   - Enables internationalization

**Expression Languages**:
- **Variable expressions** `${...}`: Use OGNL (Object-Graph Navigation Language)
  - Example: `${today}`, `${user.name}`
  - Executed on context variables map
- **Message expressions** `#{...}`: Reference externalized messages
  - Example: `#{welcome.message}`, `#{user.greeting(${user.name})}`

#### HTML5 Compatible Syntax

Thymeleaf supports HTML5-compliant `data-*` attribute syntax:

```html
<!-- Standard Thymeleaf attributes -->
<p th:text="#{home.welcome}">Welcome!</p>
<link rel="stylesheet" th:href="@{/css/gtvg.css}" />

<!-- HTML5 data-* attributes (100% valid HTML5) -->
<p data-th-text="#{home.welcome}">Welcome!</p>
<link rel="stylesheet" data-th-href="@{/css/gtvg.css}" />
```

**Why Use `data-*` Prefix?**:
- Custom `data-prefixed` attributes are allowed by HTML5 specification
- Templates become 100% valid HTML5 documents
- Useful when strict HTML5 validation is required
- Both syntaxes work identically - choose based on validation needs

**Note**: Browsers ignore all attributes they don't understand (including `th:text` and `data-th-text`)

### Standard Expression Syntax

#### 1. Variable Expressions: `${...}`

```html
<!-- Basic property access -->
<p th:text="${user.name}">User Name</p>

<!-- Safe navigation (prevents NullPointerException) -->
<p th:text="${user?.address?.city}">City</p>

<!-- Collection/Array access -->
<p th:text="${users[0].email}">First user's email</p>

<!-- Map access -->
<p th:text="${countriesByCode['ES']}">Spain</p>
<p th:text="${countriesByCode.ES}">Spain</p>
```

#### 2. Selection Expressions: `*{...}`

Used with `th:object` to reference properties of the selected object:

```html
<!-- With th:object -->
<div th:object="${user}">
    <p th:text="*{name}">Name</p>
    <p th:text="*{email}">Email</p>
    <p th:text="*{address.city}">City</p>
</div>

<!-- Equivalent without th:object -->
<div>
    <p th:text="${user.name}">Name</p>
    <p th:text="${user.email}">Email</p>
</div>

<!-- Accessing selected object with #object -->
<div th:object="${user}">
    <p th:text="${#object.name}">Name via #object</p>
</div>

<!-- Mixed usage (both $ and * work) -->
<div th:object="${session.user}">
    <!-- Selection expression (recommended with th:object) -->
    <p>Name: <span th:text="*{firstName}">Sebastian</span></p>
    
    <!-- Variable expression (also works) -->
    <p>Surname: <span th:text="${session.user.lastName}">Pepper</span></p>
    
    <!-- Using #object -->
    <p>Name: <span th:text="${#object.firstName}">Sebastian</span></p>
</div>
```

**Without `th:object` - Both syntaxes are equivalent**:
```html
<div>
    <!-- These work the same without th:object -->
    <p th:text="*{session.user.name}">Name</p>
    <p th:text="${session.user.name}">Name</p>
</div>
```

**Key Points**:
- Selection expressions `*{...}` are relative to the object set by `th:object`
- When no `th:object` is set, `*{...}` and `${...}` work identically
- The selected object is accessible as `${#object}`
- Use `*{...}` for cleaner code when working with a specific object

#### 3. Message Expressions: `#{...}`

```html
<!-- Simple message -->
<p th:text="#{home.welcome}">Welcome message</p>

<!-- Message with parameters -->
<p th:text="#{user.greeting(${user.name})}">Hello, User!</p>

<!-- HTML content (unescaped) -->
<p th:utext="#{html.content}">HTML content</p>
```

#### 4. Link URL Expressions: `@{...}`

```html
<!-- Context-relative URLs -->
<a th:href="@{/users}">All Users</a>
<a th:href="@{/products/list}">Products</a>

<!-- Path variables -->
<a th:href="@{/users/{id}(id=${user.id})}">User Profile</a>
<a th:href="@{/product/{cat}/{id}(cat=${product.category}, id=${product.id})}">Product</a>

<!-- Query parameters -->
<a th:href="@{/search(page=1,size=10)}">Search</a>
<a th:href="@{/search(q=${searchTerm},category=${cat})}">Search</a>

<!-- Absolute URLs -->
<a th:href="@{https://example.com}">External Link</a>

<!-- Protocol-relative URLs -->
<script th:src="@{//cdn.example.com/jquery.js}"></script>

<!-- Server-relative URLs (different context) -->
<a th:href="@{~/admin/dashboard}">Admin Dashboard</a>

<!-- Page-relative URLs -->
<a th:href="@{user/profile.html}">Profile</a>
<a th:href="@{../admin/dashboard.html}">Admin</a>
```

#### 5. Fragment Expressions: `~{...}`

```html
<!-- Basic fragment reference -->
<div th:replace="~{fragments/header :: header}">Header</div>

<!-- Fragment with parameters -->
<div th:replace="~{fragments/user-card :: user-card(user=${currentUser}, showDetails=true)}"></div>

<!-- Dynamic fragment selection -->
<div th:replace="${user.role == 'ADMIN'} ? 
                  ~{fragments/dashboard :: admin-dashboard} : 
                  ~{fragments/dashboard :: user-dashboard}"></div>

<!-- Inline fragment -->
<div th:fragment="alert(message, type)">
    <div th:class="'alert alert-' + ${type}" th:text="${message}"></div>
</div>
<div th:replace="~{:: alert(message='Success!', type='success')}"></div>
```

#### 6. Literal Expressions

```html
<!-- String literals -->
<p th:text="'Hello World'">Static Text</p>

<!-- Number literals -->
<p th:text="100">Number</p>

<!-- Boolean literals -->
<p th:text="true">Boolean</p>

<!-- Literal substitution (recommended for concatenation) -->
<p th:text="|Hello ${user.name}!|">Hello User!</p>
<p th:text="|Total: $${price * quantity}|">Total: $100</p>
```

#### 7. Expression Utility Objects

```html
<!-- Dates -->
<p th:text="${#dates.format(today, 'dd/MM/yyyy')}">13/02/2024</p>
<p th:text="${#dates.createNow()}">Current date</p>
<p th:text="${#dates.day(today)}">15</p>

<!-- Strings -->
<p th:text="${#strings.toUpperCase(user.name)}">JOHN</p>
<p th:text="${#strings.isEmpty(user.name)}">false</p>
<p th:text="${#strings.substring(text, 0, 10)}">First 10 chars</p>
<p th:text="${#strings.capitalize(user.name)}">John</p>

<!-- Numbers -->
<p th:text="${#numbers.formatCurrency(product.price)}">$29.99</p>
<p th:text="${#numbers.formatDecimal(value, 1, 2)}">25.50</p>
<p th:text="${#numbers.sequence(1, 5)}">[1, 2, 3, 4, 5]</p>

<!-- Lists -->
<p th:text="${#lists.size(users)}">5</p>
<p th:text="${#lists.isEmpty(products)}">false</p>
<p th:text="${#lists.contains(users, currentUser)}">true</p>

<!-- Objects -->
<p th:text="${#objects.nullSafe(user, 'defaultUser')}">Default</p>
```

#### 8. Conditional Expressions

```html
<!-- Ternary operator -->
<p th:text="${user.active} ? 'Active' : 'Inactive'">Status</p>

<!-- Elvis operator (default value) -->
<p th:text="${user.nickname} ?: 'Anonymous'">Nickname</p>

<!-- No-Operation token (_) -->
<p th:text="${user} ?: _">Keep original content if null</p>
```

#### 9. Mathematical Operations

```html
<p th:text="${quantity + 1}">Increased quantity</p>
<p th:text="${total * 1.1}">Total with tax</p>
<p th:text="${count / 2}">Half count</p>
<p th:text="${price - discount}">Final price</p>
<p th:text="${stock % 10}">Remainder</p>
```

#### 10. String Concatenation and Text Operations

```html
<!-- String concatenation with + -->
<span th:text="'The name of the user is ' + ${user.name}">User Name</span>

<!-- Literal substitution (recommended) -->
<p th:text="|Hello ${user.name}!|">Hello User!</p>
<p th:text="|Total: $${price * quantity}|">Total: $100</p>

<!-- Complex concatenation -->
<p th:text="'Welcome ' + ${user.firstName} + ' ' + ${user.lastName}">Welcome John Doe</p>
```

#### 11. Comparisons and Logical Operators

```html
<!-- Comparison operators -->
<div th:if="${prodStat.count} > 1">More than one product</div>
<div th:if="${prodStat.count} &gt; 1">HTML-escaped greater than</div>
<div th:if="${age >= 18}">Adult</div>
<div th:if="${count == 0}">Empty</div>
<div th:if="${price < 100}">Affordable</div>

<!-- Logical operators -->
<div th:if="${user.active and user.verified}">Active and verified</div>
<div th:if="${user.vip or user.admin}">Special access</div>
<div th:if="${not user.banned}">Not banned</div>

<!-- Ternary/Conditional expressions -->
<span th:text="(${execMode} == 'dev') ? 'Development' : 'Production'">Production</span>
<span th:text="${user.role == 'ADMIN'} ? 'Administrator' : 'User'">User</span>
```

### Property Access Methods

Thymeleaf provides multiple ways to access object properties:

#### 1. Dot Notation (Standard)
```html
<p th:text="${person.father.name}">Father's Name</p>
<p th:text="${user.profile.settings.theme}">Theme</p>
```

#### 2. Bracket Notation (Flexible)
```html
<!-- With string literals -->
<p th:text="${person['father']['name']}">Father's Name</p>

<!-- With dynamic property names -->
<p th:text="${user['profile']['settings'][preferenceType]}">Preference</p>

<!-- With variables as property names -->
<p th:text="${person[propertyName]}">Dynamic Property</p>
```

#### 3. Map Access
```html
<!-- Both syntaxes work for Maps -->
<p th:text="${countriesByCode.ES}">Spain</p>
<p th:text="${countriesByCode['ES']}">Spain</p>

<!-- Complex map keys -->
<p th:text="${personsByName['Stephen Zucchini'].age}">25</p>
<p th:text="${productPrices['item-123'].value}">29.99</p>
```

#### 4. Array and Collection Indexing
```html
<!-- Array access -->
<p th:text="${personsArray[0].name}">First Person</p>
<p th:text="${tags[2]}">Third Tag</p>

<!-- List access -->
<p th:text="${userList[5].email}">Sixth User Email</p>
<p th:text="${productIds[currentIndex]}">Current Product ID</p>
```

#### 5. Method Calls
```html
<!-- Method without parameters -->
<p th:text="${person.createCompleteName()}">Full Name</p>
<p th:text="${user.getDisplayName()}">Display Name</p>

<!-- Method with parameters -->
<p th:text="${person.createCompleteNameWithSeparator('-')}">First-Last</p>
<p th:text="${calculator.add(5, 10)}">15</p>
<p th:text="${stringUtils.capitalize(user.name)}">John</p>
```

#### 6. Safe Navigation Operator (`?.`)

Prevents `NullPointerException` when accessing properties of potentially null objects:

```html
<!-- Returns null instead of throwing exception -->
<p th:text="${person?.father?.name}">Father's Name</p>

<!-- Safe chain of multiple properties -->
<p th:text="${user?.profile?.settings?.theme}">Theme</p>

<!-- Safe method calls -->
<p th:text="${user?.getDisplayName()}">Display Name</p>

<!-- With Elvis operator for default value -->
<p th:text="${user?.nickname} ?: 'Anonymous'">Nickname</p>
```

#### Complete Example
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<body>
    <!-- Dot Notation -->
    <div th:object="${customer}">
        <h2 th:text="*{name}">Customer Name</h2>
        <p th:text="*{address.street}">Street</p>
        <p th:text="*{address.city}">City</p>
    </div>

    <!-- Map Access -->
    <p>Country: <span th:text="${countryMap['US']}">United States</span></p>
    <p>Currency: <span th:text="${currencies.USD}">US Dollar</span></p>

    <!-- Array Access -->
    <ul>
        <li th:each="i : ${#numbers.sequence(0, 2)}">
            <span th:text="${products[i].name}">Product Name</span>
        </li>
    </ul>

    <!-- Method Calls -->
    <p th:text="${user.getFullName()}">Full Name</p>
    <p th:text="${dateUtils.formatDate(today, 'yyyy-MM-dd')}">2024-01-15</p>

    <!-- Mixed Examples -->
    <div th:if="${user.permissions['admin']}">
        <p th:text="|Welcome ${user.profile['displayName']}!|">Welcome Admin!</p>
    </div>
</body>
</html>
```

### Context Objects

Thymeleaf provides access to web context objects:

#### 1. `#ctx` - Context Object
```html
<p th:text="${#ctx.locale}">Locale: en_US</p>
<p th:text="${#ctx.variableNames}">[user, products, ...]</p>
<p th:text="${#ctx.containsVariable('user')}">true</p>
```

#### 2. `#vars` - Context Variables
```html
<!-- Same as using direct variable names -->
<p th:text="${#vars.user.name}">John Doe</p>
<p th:text="${#vars['product'].price}">29.99</p>
<p th:text="${#vars.get('today')}">2024-01-15</p>
```

#### 3. `#locale` - Locale Information
```html
<p th:text="${#locale.country}">US</p>
<p th:text="${#locale.language}">en</p>
<p th:text="${#locale.displayName}">English (United States)</p>
<p th:text="${#locale.displayCountry}">United States</p>
<p th:text="${#locale.displayLanguage}">English</p>
```

#### 4. `#request` - HttpServletRequest
```html
<p th:text="${#request.contextPath}">/myapp</p>
<p th:text="${#request.servletPath}">/home</p>
<p th:text="${#request.serverName}">localhost</p>
<p th:text="${#request.serverPort}">8080</p>
<p th:text="${#request.scheme}">http</p>
<p th:text="${#request.queryString}">page=1</p>
<p th:text="${#request.getParameter('page')}">1</p>
<p th:text="${#request.getHeader('User-Agent')}">Browser Info</p>
```

#### 5. `#response` - HttpServletResponse
```html
<p th:text="${#response.characterEncoding}">UTF-8</p>
<p th:text="${#response.contentType}">text/html</p>
<p th:text="${#response.status}">200</p>
```

#### 6. `#session` - HttpSession
```html
<p th:text="${#session.id}">Session ID</p>
<p th:text="${#session.getAttribute('cartCount')}">5</p>
<p th:text="${#session.lastAccessedTime}">1705320000000</p>
<p th:text="${#session.maxInactiveInterval}">1800</p>

<!-- Check if session attribute exists -->
<div th:if="${#session.containsKey('user')}">
    <p>User is logged in</p>
</div>
```

#### 7. `#servletContext` - ServletContext
```html
<p th:text="${#servletContext.contextPath}">/myapp</p>
<p th:text="${#servletContext.serverInfo}">Tomcat</p>
<p th:text="${#servletContext.majorVersion}">4</p>
<p th:text="${#servletContext.minorVersion}">0</p>
```

#### Practical Examples

**Localization and Internationalization**:
```html
<div th:with="currentLocale=${#locale}">
    <p>Current Language: <span th:text="${currentLocale.language}">en</span></p>
    <p>Country: <span th:text="${currentLocale.country}">US</span></p>
    
    <!-- Show different content based on locale -->
    <div th:if="${#locale.language == 'es'}">
        <p>¡Bienvenido!</p>
    </div>
    <div th:if="${#locale.language == 'fr'}">
        <p>Bienvenue!</p>
    </div>
    <div th:if="${#locale.language == 'en'}">
        <p>Welcome!</p>
    </div>
</div>
```

**Request Parameter Handling**:
```html
<!-- Get query parameters -->
<div th:if="${#request.getParameter('search')}">
    <p>Search results for: 
        <strong th:text="${#request.getParameter('search')}">query</strong>
    </p>
</div>

<!-- Pagination with request parameters -->
<div class="pagination">
    <a th:href="@{/products(page=0, size=${#request.getParameter('size')})}">First</a>
    
    <span th:each="i : ${#numbers.sequence(1, 5)}">
        <a th:href="@{/products(page=${i}, size=${#request.getParameter('size')})}"
           th:text="${i}">1</a>
    </span>
</div>

<!-- Form with current query string -->
<form th:action="@{/search${#request.queryString != null ? '?' + #request.queryString : ''}}"
      method="post">
    <input type="text" name="filter" placeholder="Filter results">
    <button type="submit">Apply Filter</button>
</form>
```

### Setting Attributes

Thymeleaf provides two approaches for setting HTML attributes dynamically.

#### Using `th:attr` (Multiple Attributes)

Set multiple attributes at once using comma-separated pairs:

```html
<!-- Original static HTML form -->
<form action="subscribe.html">
    <fieldset>
        <input type="text" name="email" />
        <input type="submit" value="Subscribe!" />
    </fieldset>
</form>

<!-- Thymeleaf form with th:attr -->
<form action="subscribe.html" th:attr="action=@{/subscribe}">
    <fieldset>
        <input type="text" name="email" />
        <input type="submit" value="Subscribe!" th:attr="value=#{subscribe.submit}"/>
    </fieldset>
</form>

<!-- Multiple attributes at once -->
<form th:attr="action=@{/subscribe},method='post',novalidate='novalidate'">
    <input th:attr="placeholder=#{email.placeholder},maxlength=100,required='required'" />
</form>

<!-- Custom data attributes -->
<div th:attr="data-user-id=${user.id},
               data-role=${user.role},
               data-created=${#dates.format(user.createdAt, 'yyyy-MM-dd')}">
    User data container
</div>

<!-- Dynamic attributes -->
<div th:attr="class=${user.active} ? 'user-active' : 'user-inactive',
               data-status=${user.status},
               style=${user.vip} ? 'border: 2px solid gold;' : ''">
    User Card
</div>
```

#### Using Specific Attributes (Recommended)

Better approach: Use dedicated `th:*` attributes for better readability:

```html
<!-- Better approach: specific attributes -->
<form th:action="@{/subscribe}" method="post">
    <fieldset>
        <input type="text" 
               name="email" 
               th:placeholder="#{email.placeholder}"
               th:maxlength="100"
               th:required="required" />
        <input type="submit" th:value="#{subscribe.submit}" />
    </fieldset>
</form>

<!-- Dynamic attributes -->
<img th:src="@{/images/{file}(file=${user.avatar})}"
     th:alt="${user.name}"
     th:width="${avatarSize}"
     th:height="${avatarSize}" />

<!-- Conditional attributes -->
<input type="text" 
       th:value="${user.name}"
       th:readonly="${not user.canEdit}">

<button th:disabled="${#strings.isEmpty(formData)}">Submit</button>
```

#### Comparison: `th:attr` vs Specific Attributes

```html
<!-- Using th:attr (less readable) -->
<div th:attr="class=container, id=main-content, data-role=admin-panel">
    Content with th:attr
</div>

<!-- Using specific attributes (recommended) -->
<div th:class="container" 
     th:id="main-content" 
     th:attr="data-role=admin-panel">
    Content with specific attributes
</div>
```

#### Best Practices

```html
<!-- ✅ Good: Use specific attributes for standard HTML attributes -->
<input th:value="#{subscribe.submit}"/>
<a th:href="@{/users}">Users</a>
<img th:src="@{/images/logo.png}">

<!-- ✅ Good: Use th:attr for custom data-* attributes -->
<div th:attr="data-id=${user.id}, data-role=${user.role}"></div>

<!-- ❌ Avoid: Don't use th:attr for single standard attributes -->
<input th:attr="value=#{subscribe.submit}"/>  <!-- Use th:value instead -->
```

#### Complete Example

```html
<form th:action="@{/user/update}" 
      th:object="${user}"
      method="post"
      class="user-form"
      th:attr="novalidate=novalidate, data-form-version='1.0'">
    
    <div class="form-group">
        <label th:text="#{user.name}">Name</label>
        <input type="text" 
               th:field="*{name}"
               th:placeholder="#{user.name.placeholder}"
               th:required="required"
               th:maxlength="50" />
    </div>
    
    <div class="form-group">
        <label th:text="#{user.email}">Email</label>
        <input type="email" 
               th:field="*{email}"
               th:placeholder="#{user.email.placeholder}"
               th:required="required" />
    </div>
    
    <div class="form-actions">
        <button type="submit"
                th:text="#{form.save}"
                th:class="${user.new} ? 'btn-success' : 'btn-primary'"
                th:disabled="${#strings.isEmpty(user.name)}">
            Save
        </button>
        
        <button type="button"
                th:text="#{form.cancel}"
                th:onclick="|location.href='@{/users}'|"
                class="btn-secondary">
            Cancel
        </button>
    </div>
</form>
```

### Iteration with `th:each`

#### Basic Iteration

```html
<ul>
    <li th:each="product : ${products}" th:text="${product.name}">Product Name</li>
</ul>
```

#### Iteration with Status Variable

```html
<table>
    <tr th:each="product, iterStat : ${products}">
        <td th:text="${iterStat.count}">1</td>
        <td th:text="${iterStat.index}">0</td>
        <td th:text="${product.name}">Product</td>
        <td th:class="${iterStat.odd} ? 'odd' : 'even'">Odd/Even</td>
    </tr>
</table>
```

**Status Variable Properties**:

| Property | Description | Example |
|----------|-------------|---------|
| `index` | Zero-based index | 0, 1, 2, ... |
| `count` | One-based counter | 1, 2, 3, ... |
| `size` | Total elements | 5 |
| `current` | Current element | The object |
| `even` | Is even iteration? | true/false |
| `odd` | Is odd iteration? | true/false |
| `first` | Is first iteration? | true/false |
| `last` | Is last iteration? | true/false |

#### Iterating Collections

```html
<!-- List -->
<div th:each="item : ${listItems}" th:text="${item}">Item</div>

<!-- Array -->
<div th:each="element : ${arrayItems}" th:text="${element}">Element</div>

<!-- Set -->
<div th:each="value : ${setItems}" th:text="${value}">Value</div>

<!-- Map -->
<div th:each="entry : ${mapItems}">
    Key: <span th:text="${entry.key}">key</span>,
    Value: <span th:text="${entry.value}">value</span>
</div>

<!-- Number sequence -->
<div th:each="i : ${#numbers.sequence(1, 5)}" th:text="${i}">1</div>
```

### Conditional Evaluation

#### `th:if` and `th:unless`

```html
<!-- th:if - render if true -->
<div th:if="${user != null}">
    <p>Welcome, <span th:text="${user.name}">John</span>!</p>
</div>

<!-- th:unless - render if false -->
<div th:unless="${user != null}">
    <p>Please log in</p>
</div>

<!-- Complex conditions -->
<div th:if="${user.active and user.emailVerified}">
    <p>Fully verified account</p>
</div>

<div th:if="${product.stock > 0 and product.price < 100}">
    <p>Available and affordable</p>
</div>
```

#### `th:switch` and `th:case`

```html
<div th:switch="${user.role}">
    <p th:case="'ADMIN'">Administrator Access</p>
    <p th:case="'EDITOR'">Editor Access</p>
    <p th:case="'USER'">User Access</p>
    <p th:case="*">Guest Access (default)</p>
</div>

<!-- With status styling -->
<div th:switch="${order.status}">
    <span th:case="'PENDING'" class="badge badge-warning">Pending</span>
    <span th:case="'PROCESSING'" class="badge badge-info">Processing</span>
    <span th:case="'SHIPPED'" class="badge badge-primary">Shipped</span>
    <span th:case="'DELIVERED'" class="badge badge-success">Delivered</span>
    <span th:case="*" class="badge badge-secondary">Unknown</span>
</div>
```

### Local Variables with `th:with`

```html
<!-- Single variable -->
<div th:with="userName='John Doe'">
    <p th:text="${userName}">John Doe</p>
</div>

<!-- Multiple variables -->
<div th:with="firstName='John', lastName='Doe', age=30">
    <p th:text="|Name: ${firstName} ${lastName}, Age: ${age}|">Name: John Doe, Age: 30</p>
</div>

<!-- Complex calculations -->
<div th:with="total=${price * quantity}, tax=${total * 0.1}, finalTotal=${total + tax}">
    <p th:text="|Subtotal: $${total}|">Subtotal: $100</p>
    <p th:text="|Tax: $${tax}|">Tax: $10</p>
    <p th:text="|Total: $${finalTotal}|">Total: $110</p>
</div>
```

### Attribute Precedence

When multiple `th:*` attributes are on the same tag, they execute in this order:

| Order | Attribute | Purpose |
|-------|-----------|---------|
| 1 | `th:include` / `th:replace` | Fragment inclusion |
| 2 | `th:each` | Iteration |
| 3 | `th:if` / `th:unless` / `th:switch` / `th:case` | Conditionals |
| 4 | `th:object` / `th:with` | Local variables |
| 5 | `th:attr` / `th:attrprepend` / `th:attrappend` | Generic attributes |
| 6 | `th:value` / `th:href` / `th:src` / etc. | Specific attributes |
| 7 | `th:text` / `th:utext` | Text content |
| 8 | `th:fragment` | Fragment definition |
| 9 | `th:remove` | Remove tag |

**Example**:
```html
<!-- Correct: th:each before th:if -->
<li th:each="item : ${items}" 
    th:if="${item.visible}"
    th:text="${item.description}">
    Item description
</li>
```

### Complete Example

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title th:text="#{app.title}">Application Title</title>
    <link rel="stylesheet" th:href="@{/css/style.css}" />
</head>
<body>
    <!-- Header -->
    <div th:replace="~{fragments/header :: header}"></div>
    
    <!-- Messages -->
    <div th:if="${successMessage}" class="alert alert-success">
        <p th:text="${successMessage}">Success!</p>
    </div>
    
    <!-- User Profile -->
    <div th:if="${user != null}" th:object="${user}">
        <h2 th:text="|Welcome, *{name}!|">Welcome, User!</h2>
        <p th:text="|Email: *{email}|">Email: user@example.com</p>
        
        <!-- Role badge -->
        <span th:switch="*{role}">
            <span th:case="'ADMIN'" class="badge badge-danger">Admin</span>
            <span th:case="'EDITOR'" class="badge badge-warning">Editor</span>
            <span th:case="*" class="badge badge-info">User</span>
        </span>
    </div>
    
    <!-- Product List -->
    <div th:with="totalProducts=${#lists.size(products)}">
        <h3 th:text="|Products (${totalProducts})|">Products (0)</h3>
        
        <table th:if="${totalProducts > 0}">
            <thead>
                <tr>
                    <th>#</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Status</th>
                </tr>
            </thead>
            <tbody>
                <tr th:each="product, stat : ${products}"
                    th:class="${stat.odd} ? 'odd' : 'even'">
                    <td th:text="${stat.count}">1</td>
                    <td th:text="${product.name}">Product Name</td>
                    <td th:text="${#numbers.formatCurrency(product.price)}">$29.99</td>
                    <td>
                        <span th:if="${product.stock > 0}" class="in-stock">In Stock</span>
                        <span th:unless="${product.stock > 0}" class="out-of-stock">Out of Stock</span>
                    </td>
                </tr>
            </tbody>
        </table>
        
        <p th:unless="${totalProducts > 0}">No products available</p>
    </div>
    
    <!-- Footer -->
    <div th:replace="~{fragments/footer :: footer}"></div>
</body>
</html>
```

---

## Form Validation

### Server-Side Validation

#### Dependency

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

#### Validation Annotations

| Annotation | Purpose | Example |
|-----------|---------|---------|
| `@NotNull` | Field cannot be null | `@NotNull(message="Price required")` |
| `@NotBlank` | String cannot be null/empty | `@NotBlank(message="Name is mandatory")` |
| `@NotEmpty` | Collection cannot be empty | `@NotEmpty(message="List required")` |
| `@Size` | String/Collection size | `@Size(min=3, max=15, message="Name must be 3-15 chars")` |
| `@Min` | Minimum value | `@Min(value=0, message="Price must be >= 0")` |
| `@Max` | Maximum value | `@Max(value=100, message="Age must be <= 100")` |
| `@Email` | Valid email format | `@Email(message="Invalid email")` |
| `@Pattern` | Regex pattern | `@Pattern(regexp="[0-9]{10}", message="Invalid phone")` |

#### Entity with Validation

```java
@Entity
@Data
public class Product {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer pid;
    
    @Size(min = 3, max = 15, message = "Name must be between 3 and 15 characters")
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotNull(message = "Price should not be null")
    @Min(value = 0, message = "Price must be positive")
    private Double price;
    
    @NotNull(message = "Quantity should be entered")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
}
```

#### Controller with Validation

```java
@Controller
public class ProductController {
    
    @Autowired
    public ProductRepository repo;
    
    @GetMapping("/")
    public String loadForm(Model model) {
        model.addAttribute("p", new Product());
        return "index";
    }
    
    @PostMapping("/product")
    public String saveProduct(@Validated @ModelAttribute("p") Product p, 
                            BindingResult result, Model model) {
        
        if (result.hasErrors()) {
            // Validation failed - return to form
            return "index";
        }
        
        Product savedProduct = repo.save(p);
        if (savedProduct.getPid() != null) {
            model.addAttribute("msg", "Product saved successfully");
        }
        return "index";
    }
}
```

**Important**:
- `@Validated` enables validation
- `BindingResult` must be immediately after validated parameter
- If validation fails, `result.hasErrors()` returns true

#### Display Validation Errors (Thymeleaf)

```html
<form th:action="@{/product}" th:object="${p}" method="post">
    <table>
        <tr>
            <td>Name:</td>
            <td><input type="text" th:field="*{name}"></td>
            <td th:if="${#fields.hasErrors('name')}" 
                th:errors="*{name}" class="text-danger"></td>
        </tr>
        <tr>
            <td>Price:</td>
            <td><input type="number" th:field="*{price}"></td>
            <td th:if="${#fields.hasErrors('price')}" 
                th:errors="*{price}" class="text-danger"></td>
        </tr>
        <tr>
            <td>Quantity:</td>
            <td><input type="number" th:field="*{quantity}"></td>
            <td th:if="${#fields.hasErrors('quantity')}" 
                th:errors="*{quantity}" class="text-danger"></td>
        </tr>
    </table>
</form>
```

---

## Exception Handling

### Types of Exception Handling

1. **Local Exception Handling** - Controller-specific
2. **Global Exception Handling** - Application-specific (Recommended)

### Local Exception Handling

```java
@Controller
public class DemoController {
    
    private Logger logger = LoggerFactory.getLogger(DemoController.class);
    
    @GetMapping("/")
    public String getMessage(Model model) {
        int i = 1 / 0;  // ArithmeticException
        model.addAttribute("msg", "Hi, Hello");
        return "index";
    }
    
    @GetMapping("/greet")
    public String greetMsg(Model model) {
        String txt = null;
        txt.length();  // NullPointerException
        model.addAttribute("msg", "Greetings");
        return "index";
    }
    
    // Local exception handler
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        String msg = e.getMessage();
        logger.error(msg);
        return "errorPage";
    }
}
```

**Problem**: Code duplication if same handler needed in multiple controllers.

### Global Exception Handling (Recommended)

```java
@ControllerAdvice
public class AppExceptionHandler {
    
    private Logger logger = LoggerFactory.getLogger(AppExceptionHandler.class);
    
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        String errorText = e.getMessage();
        logger.error(errorText);
        return "errorPage";
    }
    
    @ExceptionHandler(value = NullPointerException.class)
    public String handleNullPointerEx(NullPointerException e) {
        String errorText = e.getMessage();
        logger.error(errorText);
        return "errorPage";
    }
}
```

### Error Page (errorPage.html)

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Error Page</title>
</head>
<body>
    <h2>Some problem occurred, Please try after some time....</h2>
    <a href="/">Go to Home</a>
</body>
</html>
```

### Custom Exception

```java
public class UserNotFoundException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public UserNotFoundException() {
        super();
    }
    
    public UserNotFoundException(String msg) {
        super(msg);
    }
}
```

### Using Custom Exception

```java
@RestController
public class UserRestController {
    
    @GetMapping("/user/{userId}")
    public String getUserName(@PathVariable("userId") Integer userId) 
            throws UserNotFoundException {
        
        if (userId == 100) {
            return "John";
        } else if (userId == 200) {
            return "Smith";
        } else {
            throw new UserNotFoundException("User not found with ID: " + userId);
        }
    }
}
```

---

*Continue to [05-REST-API.md](05-REST-API.md) for REST API development.*
