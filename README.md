# Finance Dashboard Backend

A production-structured Spring Boot REST API with JWT authentication and role-based access control (RBAC) for managing financial records and dashboard analytics.

\---

## Tech Stack

|Layer|Technology|
|-|-|
|Framework|Spring Boot |
|Security|Spring Security + JWT (jjwt 0.12)|
|Persistence|Spring Data JPA + H2 (dev)|
|Validation|Jakarta Bean Validation|
|Java Version|Java 21|

\---

## Quick Start (IntelliJ)

1. **Open the project** — File → Open → select the `finance-dashboard` folder
2. **Trust the project** when prompted
3. **Wait for Maven** to download dependencies (bottom progress bar)
4. **Run** `FinanceDashboardApplication.java` (right-click → Run)
5. **API is live** at `http://localhost:8080`
6. **H2 Console** at `http://localhost:8080/h2-console`

   * JDBC URL: `jdbc:h2:mem:financedb`
   * User: `sa` | Password: *(blank)*

\---

## Default Seeded Users

|Email|Password|Role|
|-|-|-|
|admin@finance.com|admin123|ADMIN|
|analyst@finance.com|analyst123|ANALYST|
|viewer@finance.com|viewer123|VIEWER|

\---

## Role Permissions Matrix

|Action|VIEWER|ANALYST|ADMIN|
|-|-|-|-|
|Login / Register|✅|✅|✅|
|View dashboard summary|❌|✅|✅|
|View financial records|❌|✅|✅|
|Create financial records|❌|❌|✅|
|Update financial records|❌|❌|✅|
|Delete financial records|❌|❌|✅|
|Manage users (CRUD)|❌|❌|✅|

\---

## API Endpoints

### Authentication (Public)

```
POST /api/auth/register     Register a new user
POST /api/auth/login        Login and receive JWT token
GET  /api/health            Health check
```

**Register Body:**

```json
{
  "fullName": "Jane Doe",
  "email": "jane@example.com",
  "password": "securepass",
  "role": "VIEWER"
}
```

**Login Body:**

```json
{
  "email": "admin@finance.com",
  "password": "admin123"
}
```

**Login Response:**

```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tokenType": "Bearer",
  "user": { "id": 1, "fullName": "Admin User", "role": "ADMIN" }
}
```

> All subsequent requests need the header:
> `Authorization: Bearer <token>`

\---

### User Management (ADMIN only)

```
GET    /api/users                    List all users
GET    /api/users/{id}               Get user by ID
GET    /api/users/by-role?role=ANALYST  Filter by role
PATCH  /api/users/{id}/role          Update role
PATCH  /api/users/{id}/status        Activate / deactivate
DELETE /api/users/{id}               Delete user
```

**Update Role Body:**

```json
{ "role": "ANALYST" }
```

**Update Status Body:**

```json
{ "active": false }
```

\---

### Financial Records (ADMIN write / ANALYST+ADMIN read)

```
POST   /api/records                  Create record           \[ADMIN]
GET    /api/records                  List / filter records   \[ANALYST, ADMIN]
GET    /api/records/{id}             Get single record       \[ANALYST, ADMIN]
PUT    /api/records/{id}             Update record           \[ADMIN]
DELETE /api/records/{id}             Delete record           \[ADMIN]
```

**Create / Update Record Body:**

```json
{
  "amount": 1500.00,
  "type": "EXPENSE",
  "category": "Rent",
  "date": "2024-04-01",
  "notes": "Monthly rent payment"
}
```

**Filtering with Query Params:**

```
GET /api/records?type=INCOME
GET /api/records?category=Groceries
GET /api/records?startDate=2024-01-01\&endDate=2024-03-31
GET /api/records?type=EXPENSE\&startDate=2024-01-01\&endDate=2024-03-31
```

\---

### Dashboard Analytics (ANALYST + ADMIN)

```
GET /api/dashboard/summary                              Full summary (all time)
GET /api/dashboard/summary/range?start=...\&end=...      Summary for date range
```

**Summary Response Shape:**

```json
{
  "totalIncome": 17250.00,
  "totalExpenses": 8577.00,
  "netBalance": 8673.00,
  "incomeByCategory": {
    "Salary": 15000.00,
    "Freelance": 2000.00,
    "Investments": 750.00
  },
  "expensesByCategory": {
    "Rent": 4500.00,
    "Groceries": 920.00,
    "Utilities": 222.00
  },
  "monthlyTrends": \[
    { "year": 2024, "month": 4, "income": 5450.00, "expenses": 2722.00, "net": 2728.00 }
  ],
  "recentActivity": \[ ... ]
}
```

\---

## Project Structure

```
src/main/java/com/finance/dashboard/
├── FinanceDashboardApplication.java
├── config/
│   ├── SecurityConfig.java          ← JWT filter chain + role-based URL rules
│   └── DataSeeder.java              ← Seeds DB on startup
├── controller/
│   ├── AuthController.java
│   ├── UserController.java
│   ├── FinancialRecordController.java
│   ├── DashboardController.java
│   └── HealthController.java
├── dto/
│   ├── request/                     ← Validated input DTOs
│   └── response/                    ← Clean output DTOs
├── entity/
│   ├── User.java                    ← Implements UserDetails
│   ├── FinancialRecord.java
│   └── Role.java (enum)
├── exception/
│   ├── GlobalExceptionHandler.java  ← Centralised error responses
│   ├── ResourceNotFoundException.java
│   ├── DuplicateResourceException.java
│   └── AccessDeniedException.java
├── repository/
│   ├── UserRepository.java
│   └── FinancialRecordRepository.java   ← Custom aggregation queries
├── security/
│   ├── JwtTokenProvider.java
│   └── JwtAuthenticationFilter.java
└── service/
    ├── UserService.java (interface)
    ├── FinancialRecordService.java (interface)
    ├── DashboardService.java (interface)
    └── impl/
        ├── AuthService.java
        ├── UserDetailsServiceImpl.java
        ├── UserServiceImpl.java
        ├── FinancialRecordServiceImpl.java
        └── DashboardServiceImpl.java
```

\---

## Switching to PostgreSQL (Production)

1. Add the PostgreSQL driver to `pom.xml`:

```xml
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. In `application.properties`, comment out the H2 block and uncomment the PostgreSQL block:

```properties

\# Database - H2 (easy local dev; swap for PostgreSQL below)

spring.datasource.url=jdbc:h2:mem:financedb;DB\_CLOSE\_DELAY=-1;DB\_CLOSE\_ON\_EXIT=FALSE

spring.datasource.driver-class-name=org.h2.Driver

spring.datasource.username=sa

spring.datasource.password=

spring.h2.console.enabled=true

spring.h2.console.path=/h2-console```

\---

## Error Response Format

All errors follow a consistent envelope:

```json
{
  "status": 400,
  "error": "Validation failed",
  "message": "One or more fields are invalid",
  "timestamp": "2024-04-01T10:30:00",
  "fieldErrors": {
    "amount": "Amount must be at least 0.01",
    "date": "Date cannot be in the future"
  }
}
```

|Status|Meaning|
|-|-|
|400|Validation error / bad request|
|401|Bad credentials / inactive account|
|403|Insufficient role permissions|
|404|Resource not found|
|409|Duplicate email|
|500|Unexpected server error|

\---

## Running Tests

```bash
mvn test
```

Tests cover: registration, login, invalid input, role-based access denial.

