# BONUS TASK — Caching Layer Implementation

## 📌 Overview
In-memory caching system using **Singleton pattern** for the Hogwarts Management API.
Caches frequently accessed data to improve performance and reduce database queries.

---

## ✅ Requirements Checklist

| Requirement | Status | Implementation |
|-------------|--------|----------------|
| In-memory cache (Map) | ✅ | `ConcurrentHashMap<String, CacheEntry>` |
| Cache commonly used method | ✅ | `getAllStudents()` and `getAllHouses()` |
| Avoid repeated DB queries | ✅ | Cache HIT returns data without SQL |
| Singleton pattern | ✅ | `CacheService.getInstance()` |
| Manual cache clearing | ✅ | 4 admin endpoints |
| Auto-invalidation | ✅ | On create/update/delete operations |
| SOLID principles | ✅ | SRP, OCP, DIP followed |
| Layered architecture | ✅ | Cache in Service layer only |

---

 
 ##  Files Created/Modified

### New Files (2)
1. **`CacheService.java`** → `src/main/java/kz/hogwarts/patterns/singleton/`
   - Singleton cache instance
   - Thread-safe using `ConcurrentHashMap`
   - Methods: `get()`, `put()`, `evict()`, `clear()`

2. **`AdminController.java`** → `src/main/java/kz/hogwarts/controller/`
   - Manual cache management endpoints
   - Stats, clear, check operations

### Modified Files (2)
3. **`StudentService.java`** → Added caching to `getAllStudents()`
4. **`HouseService.java`** → Added caching to `getAllHouses()` and `getHouseById()`

---

## Testing Instructions

### Test 1: Verify Cache HIT/MISS
```bash
# Terminal 1: Start application and watch logs
mvn spring-boot:run

# Terminal 2: Send requests
curl http://localhost:8080/api/students  # First request
curl http://localhost:8080/api/students  # Second request
```

**Expected Logs:**
```
[DEBUG] Cache MISS: students:all
[DEBUG] Fetching all students          ← SQL executed
[DEBUG] Cache PUT: students:all (size: 1)

[DEBUG] Cache HIT: students:all        ← No SQL! Data from cache
```

### Test 2: Verify Auto-Invalidation
```bash
# 1. Populate cache
curl http://localhost:8080/api/students

# 2. Create new student
curl -X POST http://localhost:8080/api/students \
  -H "Content-Type: application/json" \
  -d '{"name":"Luna Lovegood","age":15,"year":5,"houseId":3}'

# 3. Request again
curl http://localhost:8080/api/students
```

**Expected Logs:**
```
[DEBUG] Cache HIT: students:all
[INFO] Created student with id: 5
[INFO] Cache EVICT BY PREFIX: students: (1 entries removed)
[DEBUG] Cache MISS: students:all       ← Cache was cleared!
```

### Test 3: Manual Cache Management
```bash
# Check cache stats
curl http://localhost:8080/api/admin/cache/stats

# Clear entire cache
curl -X DELETE http://localhost:8080/api/admin/cache/clear

# Verify empty
curl http://localhost:8080/api/admin/cache/stats
# Output: "cacheSize": 0
```

---




# Hogwarts Endterm Project (Spring Boot REST API)

## Project Overview
This project is a Spring Boot REST API for managing a Hogwarts Management System using PostgreSQL.
It provides CRUD operations for key entities: Houses, Students, Professors, Courses, and Enrollments.

The application follows layered architecture:
Controller → Service → Repository → PostgreSQL (Spring JDBC / JdbcTemplate).

Main goals:
- implement REST API with proper HTTP status codes
- integrate PostgreSQL database
- demonstrate creational design patterns (Singleton, Factory, Builder)
- apply SOLID and OOP principles in a clean architecture

## Technology Stack

- Java 17+  
- Spring Boot 3.x  
- Spring JDBC (JdbcTemplate)  
- PostgreSQL  
- Maven  
- Postman (API testing)  

Additional tools:
- pgAdmin (database management)
- IntelliJ IDEA (development environment)

## REST API Documentation

Base URL:
http://localhost:8081/api

---

### Houses

GET /houses  
→ Returns list of all houses

GET /houses/{id}  
→ Returns house by id

POST /houses  
→ Creates new house  
Example JSON:
{
  "name": "TestHouse",
  "founder": "Test Founder",
  "points": 10
}

PUT /houses/{id}  
→ Updates existing house

DELETE /houses/{id}  
→ Deletes house by id

---

### Students

GET /students  
→ Returns list of students

GET /students/{id}  
→ Returns student by id

GET /students/house/{houseId}  
→ Returns students by house

POST /students  
Example JSON:
{
  "name": "Test Student",
  "age": 11,
  "houseId": 1,
  "year": 1,
  "patronus": "Fox"
}

PUT /students/{id}  
DELETE /students/{id}

---

### Professors

GET /professors  
GET /professors/{id}  
POST /professors  
PUT /professors/{id}  
DELETE /professors/{id}

---

### Courses

GET /courses  
GET /courses/{id}  
POST /courses  
PUT /courses/{id}  
DELETE /courses/{id}

---

### Enrollments

GET /enrollments  
GET /enrollments/{id}  
GET /enrollments/student/{studentId}  
GET /enrollments/course/{courseId}  
POST /enrollments  
PUT /enrollments/{id}  
DELETE /enrollments/{id}

## Error Handling

The application uses a global exception handler implemented with `@ControllerAdvice`.

All exceptions are caught in one centralized class (`GlobalExceptionHandler`) and returned as structured JSON responses.

Example error response:

{
  "status": 404,
  "message": "Student not found with id: 999999",
  "timestamp": "2026-02-09T11:50:10",
  "path": "/api/students/999999"
}

Handled cases:
- ResourceNotFoundException → 404
- DuplicateResourceException → 409
- Validation errors → 400
- Unexpected errors → 500

This ensures the API never crashes and always returns meaningful error messages.

## Design Patterns Implementation

The project implements three creational design patterns:

### 1. Singleton Pattern
Implemented in `LoggingService`.

- Ensures only one instance of the logging service exists.
- Accessed via `LoggingService.getInstance()`.
- Used across repositories for centralized logging.

Purpose:
Provides controlled access to a shared logging component.

---

### 2. Factory Pattern
Implemented in `PersonFactory`.

- Responsible for creating `Student` and `Professor` objects.
- Returns the base type `Person` to support polymorphism.
- Used in `StudentService` and `ProfessorService` for object creation.

Purpose:
Centralizes object creation and improves extensibility.

---

### 3. Builder Pattern
Implemented in `EnrollmentBuilder`.

- Used to construct `Enrollment` objects.
- Supports optional fields.
- Provides fluent method chaining.
- Validates required fields before object creation.

Purpose:
Improves readability and prevents creation of invalid objects.

## Component Principles & Architecture

The project follows layered architecture:

Controller → Service → Repository → Database

Each layer has a clear responsibility:

- Controller: Handles HTTP requests and responses.
- Service: Contains business logic and validation.
- Repository: Executes SQL queries using JdbcTemplate.
- Database: PostgreSQL stores application data.

---

### Component Principles

The project follows three main component principles:

1. REP (Reuse/Release Equivalence Principle)
   Related classes are grouped into reusable modules (e.g., repository, service, patterns).

2. CCP (Common Closure Principle)
   Classes that change together are placed in the same package.
   For example, enrollment-related logic is grouped together.

3. CRP (Common Reuse Principle)
   Components depend only on what they use.
   Controllers depend on Services, Services depend on Repositories.

---

### Package Structure

- controller
- service
- repository
- model
- dto
- exception
- patterns
- utils

This structure improves maintainability, scalability, and separation of concerns.

## SOLID & OOP Integration

The project applies core Object-Oriented Programming principles and SOLID principles.

### OOP Principles

1. Encapsulation  
   Fields are private and accessed via getters/setters.

2. Inheritance  
   `Student` and `Professor` extend the abstract class `Person`.

3. Polymorphism  
   `PersonFactory` returns the base type `Person`, allowing polymorphic behavior.

4. Abstraction  
   Business logic is separated into services, and database logic into repositories.

---

### SOLID Principles

1. Single Responsibility Principle (SRP)  
   - Controllers handle HTTP.
   - Services contain business logic.
   - Repositories handle database operations.

2. Open/Closed Principle (OCP)  
   - New person types can be added by extending `Person` and updating `PersonFactory`.

3. Liskov Substitution Principle (LSP)  
   - `Student` and `Professor` can be used wherever `Person` is expected.

4. Interface Segregation Principle (ISP)  
   - DTOs are separated from models.
   - Responsibilities are divided into focused classes.

5. Dependency Inversion Principle (DIP)  
   - Services depend on abstractions (repositories injected via constructor).
   - Dependency Injection is handled by Spring.


## Screenshots

Postman test results are available in the `docs/screenshots` folder:

- postman_get_houses.png
- postman_create_house.png
- postman_update_house.png
- postman_delete_house.png
- postman_error_handling.png
- postman_get_students.png
- postman_create_student.png
- postman_update_student.png
- postman_delete_student.png

Architecture diagram:
- docs/architecture_diagram.png

## Reflection

During this project, I improved my understanding of:

- Spring Boot REST API development
- Working with PostgreSQL using Spring JDBC
- Implementing design patterns in real-world scenarios
- Applying SOLID and OOP principles in layered architecture

Challenges faced:

- Handling PostgreSQL generated IDs correctly
- Designing clean layered architecture
- Implementing consistent error handling
- Ensuring proper use of design patterns across services

This project strengthened my ability to design maintainable, scalable backend systems.

