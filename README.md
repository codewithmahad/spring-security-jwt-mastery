<div align="center">

<!-- ===== HEADER (SPRING BRAND STYLE) ===== -->
  <img src="https://capsule-render.vercel.app/api?type=waving&color=6DB33F&height=230&section=header&text=Enterprise%20Spring%20Boot%20Template&fontSize=48&fontColor=FFFFFF&animation=fadeIn&fontAlignY=45" width="100%" alt="Enterprise Spring Boot Banner" />

### Production-Ready Spring Boot Backend Starter

A structured backend foundation that removes repetitive setup and enforces a clean, production-style architecture from the first commit.

<p>
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white" />
  <img src="https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white" />
</p>


</div>

---

## Architecture

The project follows a layered architecture with strict separation of concerns.

```
com.yourname.project
├── config        → application configuration and infrastructure beans
├── controller    → HTTP layer (request/response handling only)
├── service       → business logic orchestration
├── repository    → persistence abstraction
├── entity        → database domain models
├── dto           → request / response contracts
├── exception     → centralized error handling
├── util          → reusable helpers
└── common        → shared components
```

Controllers remain thin.  
Business logic lives in services.  
Entities never leak into external API contracts.

This structure mirrors real production systems and scales cleanly as features grow.

---

## Quick Start

### 1. Generate a New Project

Use GitHub’s **Use this template** button, then clone:

```bash
git clone https://github.com/your-username/your-project-name.git

```

---

### 2. Rename the Project

Update:

**pom.xml**
- groupId  
- artifactId  
- name  

Refactor base package:

```
com.yourname.template → com.yourname.project

```

Rename the main application class accordingly.

---

### 3. Configure Database

Edit:

```
src/main/resources/application.yml

```

Example:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/your_db
    username: root
    password: password

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: true
```

---

### 4. Create Database Schema

Schema changes are managed through Flyway migrations.

Create:

```
src/main/resources/db/migration/V1__init.sql

```

Example:

```sql
CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);
```

---

### 5. Run the Application

```bash
mvn spring-boot:run

```

API documentation:
```
http://localhost:8080/swagger-ui.html

```

---

### 6 Add/Update Dependencies (Optional)
If your new project requires JWT Security, Redis, or Docker compose, simply open the `pom.xml` and add the Maven dependencies. MapStruct and Lombok are already configured to compile flawlessly together in the `<build>` plugins section.

---

## Engineering Decisions

This template enforces several non-negotiable practices:

- Explicit database migrations
- DTO isolation avoiding entity exposure
- Centralized exception handling
- Predictable API response structure
- Thin controllers, structured service layer
- Configuration separated from business logic

These constraints keep the codebase maintainable as it grows.

---

## Technology Stack

<table>
<tr>
<td><b>Spring Boot</b></td>
<td>Application framework and runtime</td>
</tr>
<tr>
<td><b>Spring Web</b></td>
<td>REST API layer</td>
</tr>
<tr>
<td><b>Spring Data JPA</b></td>
<td>Persistence abstraction</td>
</tr>
<tr>
<td><b>Hibernate</b></td>
<td>ORM implementation</td>
</tr>
<tr>
<td><b>Flyway</b></td>
<td>Versioned database migrations</td>
</tr>
<tr>
<td><b>MapStruct</b></td>
<td>Compile-time DTO mapping</td>
</tr>
<tr>
<td><b>Lombok</b></td>
<td>Boilerplate reduction</td>
</tr>
<tr>
<td><b>OpenAPI / Swagger</b></td>
<td>API documentation</td>
</tr>
</table>

---

## 👤 Author

Shaikh Mahad  
Backend Developer — Java & Spring Boot

This template was created to standardize backend architecture and accelerate development while practicing production-grade engineering patterns.

If this project helps you, consider giving it a ⭐

---

## 📄 License

Open-source and available for learning and professional use.

---

<div align="center">

<img src="https://img.shields.io/badge/Spring_Boot_4.0.3-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" width="900" alt="Spring Boot 4.0.3 Banner" />

</div>
