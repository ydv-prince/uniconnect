<div align="center">

# 🎓 UniConnect Backend

### *Secure, Scalable REST API for University Community & Student Networking*

[![Java Version](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.13-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Spring Security](https://img.shields.io/badge/Spring_Security-6.x-6DB33F?style=for-the-badge&logo=springsecurity&logoColor=white)](https://spring.io/projects/spring-security)
[![JWT](https://img.shields.io/badge/JWT-JJWT_0.11.5-black?style=for-the-badge&logo=jsonwebtokens&logoColor=white)](https://jwt.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-4479A1?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)
[![Maven](https://img.shields.io/badge/Maven-Build-C71A36?style=for-the-badge&logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![License](https://img.shields.io/badge/License-MIT-blue?style=for-the-badge)](LICENSE)

---

</div>

---

## 📖 Table of Contents

- [Overview](#-overview)
- [Key Features](#-key-features)
- [Architecture & Tech Stack](#-architecture--tech-stack)
- [Project Directory Structure](#-project-directory-structure)
- [Authentication Workflow](#-authentication-workflow)
- [Database Configuration](#-database-configuration)
- [Getting Started](#-getting-started)
  - [Prerequisites](#prerequisites)
  - [Installation & Setup](#installation--setup)
  - [Running the Application](#running-the-application)
- [API Endpoints Reference](#-api-endpoints-reference)
  - [Health Check](#1-health-check)
  - [User Registration](#2-user-registration)
  - [User Login](#3-user-login)
  - [Protected Profile](#4-protected-user-profile)
- [Exception Handling & Responses](#-exception-handling--responses)
- [Future Roadmap](#-future-roadmap)
- [Author & Acknowledgments](#-author--acknowledgments)

---

## 🌟 Overview

**UniConnect Backend** is the core server-side engine powering the UniConnect campus networking ecosystem. It empowers university students, faculty, and campus communities to seamlessly communicate, collaborate, and share academic information.

Engineered with **Spring Boot 3.5**, **Java 21**, and **Spring Security 6**, the backend enforces strict stateless token-based authorization via **JSON Web Tokens (JWT)** and **BCrypt** password encryption, paired with **Spring Data JPA / Hibernate** for robust MySQL relational persistence.

---

## ✨ Key Features

- 🔐 **Stateless JWT Authentication**: Secure user login with HMAC-SHA256 signed JSON Web Tokens (1-hour expiration).
- 🛡️ **BCrypt Password Hashing**: Passwords securely salted and hashed using industry-standard `BCryptPasswordEncoder`.
- ⚡ **Spring Security 6 Integration**: Granular route-level access control, custom `JwtFilter` request interception, and disabled CSRF for stateless REST execution.
- 📦 **Standardized API Responses**: Unified `ApiResponse<T>` contract (`success`, `message`, `data`) across all endpoints.
- 🚨 **Global Exception Handling**: Centralized controller advice (`@RestControllerAdvice`) capturing runtime exceptions and producing clean JSON error payloads.
- 🗄️ **Relational Persistence**: MySQL schema auto-generation (`hibernate.ddl-auto=update`) and queries via `JpaRepository`.
- 🧩 **Clean Layered Architecture**: Clear separation of concerns between Controllers, Services, Repositories, DTOs, and Entity models.

---

## 🛠️ Architecture & Tech Stack

| Component | Technology | Version | Purpose |
|---|---|---|---|
| **Language** | Java | 21 (LTS) | Core programming language |
| **Framework** | Spring Boot | 3.5.13 | Application backend runtime |
| **Security** | Spring Security | 6.x | Authorization & access control |
| **Tokens** | JJWT (`jjwt-api`, `jjwt-impl`, `jjwt-jackson`) | 0.11.5 | JWT token creation & claim parsing |
| **ORM / Data** | Spring Data JPA / Hibernate | 3.5.x | Database ORM & repository abstraction |
| **Database** | MySQL Server | 8.0+ | Relational data persistence |
| **Boilerplate** | Project Lombok | Latest | Code generation (getters, setters, constructors) |
| **Build Tool** | Apache Maven | Wrapper included | Dependency management and packaging |

---

## 📂 Project Directory Structure

```text
uniconnect-backend/
├── .mvn/
│   └── wrapper/
│       └── maven-wrapper.properties   # Maven wrapper configuration
├── src/
│   ├── main/
│   │   ├── java/com/uniconnect/uniconnectbackend/
│   │   │   ├── config/
│   │   │   │   ├── GlobalExceptionHandler.java  # Centralized REST exception handler
│   │   │   │   ├── JwtFilter.java               # Custom OncePerRequestFilter for Bearer token
│   │   │   │   ├── JwtUtil.java                 # Token generation, extraction & HMAC signing
│   │   │   │   └── SecurityConfig.java          # Spring Security filter chain configuration
│   │   │   ├── controller/
│   │   │   │   ├── TestController.java          # Root health check endpoint
│   │   │   │   └── UserController.java          # Auth & user profile REST controllers
│   │   │   ├── dto/
│   │   │   │   ├── ApiResponse.java             # Generic API wrapper payload
│   │   │   │   ├── LoginRequest.java            # Credentials input DTO
│   │   │   │   ├── RegisterRequest.java         # Registration input DTO
│   │   │   │   └── UserResponse.java            # Sanitized user response DTO
│   │   │   ├── model/
│   │   │   │   └── User.java                    # JPA User entity mapping to database table
│   │   │   ├── repository/
│   │   │   │   └── UserRepository.java          # Spring Data JPA repository with findByEmail
│   │   │   ├── service/
│   │   │   │   └── UserService.java             # Business logic, BCrypt hashing & JWT issuance
│   │   │   └── UniconnectBackendApplication.java # Spring Boot entry application class
│   │   └── resources/
│   │       └── application.properties           # Database, server & Hibernate properties
│   └── test/
│       └── java/com/uniconnect/uniconnectbackend/
│           └── UniconnectBackendApplicationTests.java # Context loading integration tests
├── .gitattributes                               # Line ending configurations
├── .gitignore                                   # Ignore list for build artifacts & IDE files
├── mvnw                                         # Unix Maven wrapper executable
├── mvnw.cmd                                     # Windows Maven wrapper executable
├── pom.xml                                      # Maven project configuration & dependencies
└── README.md                                    # Project documentation
```

---

## 🔄 Authentication Workflow

```mermaid
sequenceDiagram
    autonumber
    actor Client
    participant Controller as UserController
    participant Service as UserService
    participant DB as MySQL DB
    participant JWT as JwtUtil
    participant Filter as JwtFilter

    Note over Client, DB: Registration Flow
    Client->>Controller: POST /api/users/register (name, email, password)
    Controller->>Service: register(user)
    Service->>Service: Hash password with BCrypt
    Service->>DB: Save User Entity
    DB-->>Service: User Saved
    Service-->>Controller: Return UserResponse (id, name, email)
    Controller-->>Client: 200 OK + ApiResponse<UserResponse>

    Note over Client, DB: Login & Token Flow
    Client->>Controller: POST /api/users/login (email, password)
    Controller->>Service: login(email, password)
    Service->>DB: findByEmail(email)
    DB-->>Service: User Record
    Service->>Service: Verify BCrypt password match
    Service->>JWT: generateToken(email)
    JWT-->>Service: Signed JWT String
    Service-->>Controller: Return JWT Token
    Controller-->>Client: 200 OK + ApiResponse<String> (Token)

    Note over Client, DB: Accessing Protected Endpoint
    Client->>Filter: GET /api/users/profile with Authorization: Bearer <Token>
    Filter->>JWT: extractEmail(token)
    JWT-->>Filter: Validated Email Subject
    Filter->>Controller: Forward request
    Controller-->>Client: 200 OK + ApiResponse<String> ("Access granted")
```

---

## 🗄️ Database Configuration

UniConnect uses **MySQL** as its primary relational database.

1. **Log in to MySQL:**
   ```bash
   mysql -u root -p
   ```

2. **Create the Database:**
   ```sql
   CREATE DATABASE IF NOT EXISTS uniconnect_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   ```

3. **Verify Settings in `src/main/resources/application.properties`:**
   ```properties
   spring.application.name=uniconnect-backend
   server.port=8083

   # MySQL Connection
   spring.datasource.url=jdbc:mysql://localhost:3306/uniconnect_db
   spring.datasource.username=root
   spring.datasource.password=12345

   # Hibernate JPA
   spring.jpa.hibernate.ddl-auto=update
   spring.jpa.show-sql=true
   spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect
   ```

> [!TIP]
> Hibernate will automatically create and update the `user` table upon application startup when `spring.jpa.hibernate.ddl-auto=update` is enabled.

---

## 🚀 Getting Started

### Prerequisites

Ensure you have the following installed:
- **Java Development Kit (JDK) 21** or later
- **MySQL Server 8.0+**
- **Git**
- *(Optional)* **Postman** or **cURL** for testing

### Installation & Setup

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/ydv-prince/uniconnect.git
   cd uniconnect
   ```

2. **Configure Database Credentials:**
   Update `src/main/resources/application.properties` with your local MySQL username and password if different from the defaults.

3. **Build the Project:**
   - **Linux / macOS:**
     ```bash
     ./mvnw clean install
     ```
   - **Windows:**
     ```powershell
     .\mvnw.cmd clean install
     ```

### Running the Application

- **Using Maven Wrapper:**
  ```powershell
  .\mvnw.cmd spring-boot:run
  ```
- **Or run the compiled JAR:**
  ```powershell
  java -jar target/uniconnect-backend-0.0.1-SNAPSHOT.jar
  ```

The server will start on port **`8083`**:
`http://localhost:8083`

---

## 📡 API Endpoints Reference

| Method | Endpoint | Description | Auth Required |
|---|---|---|:---:|
| `GET` | `/` | Service health check | ❌ No |
| `POST` | `/api/users/register` | Register a new user account | ❌ No |
| `POST` | `/api/users/login` | Authenticate user & receive JWT | ❌ No |
| `GET` | `/api/users/profile` | Access protected user profile | ✅ Yes (Bearer) |

---

### 1. Health Check
Checks if the backend service is up and responding.

- **Request:**
  ```http
  GET http://localhost:8083/
  ```

- **Response (`200 OK`):**
  ```text
  UniConnect Backend is Running 🚀
  ```

---

### 2. User Registration
Registers a new user, hashes the password via BCrypt, and persists the record.

- **Endpoint:** `POST /api/users/register`
- **Headers:** `Content-Type: application/json`
- **Request Body:**
  ```json
  {
    "name": "Prince Kumar",
    "email": "pkumar052@rku.ac.in",
    "password": "SecurePassword123!"
  }
  ```

- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "User registered successfully",
    "data": {
      "id": 1,
      "name": "Prince Kumar",
      "email": "pkumar052@rku.ac.in"
    }
  }
  ```

- **cURL Example:**
  ```bash
  curl -X POST http://localhost:8083/api/users/register \
       -H "Content-Type: application/json" \
       -d '{"name":"Prince Kumar","email":"pkumar052@rku.ac.in","password":"SecurePassword123!"}'
  ```

---

### 3. User Login
Verifies user credentials and returns a signed JWT token valid for 1 hour.

- **Endpoint:** `POST /api/users/login`
- **Headers:** `Content-Type: application/json`
- **Request Body:**
  ```json
  {
    "email": "pkumar052@rku.ac.in",
    "password": "SecurePassword123!"
  }
  ```

- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "Login successful",
    "data": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJwa3VtYXIwNTJAcmt1LmFjLmluIiwiaWF0IjoxNzE2OTk5OTk5LCJleHAiOjE3MTcwMDM1OTl9..."
  }
  ```

- **cURL Example:**
  ```bash
  curl -X POST http://localhost:8083/api/users/login \
       -H "Content-Type: application/json" \
       -d '{"email":"pkumar052@rku.ac.in","password":"SecurePassword123!"}'
  ```

---

### 4. Protected User Profile
Demonstrates token validation through `JwtFilter`.

- **Endpoint:** `GET /api/users/profile`
- **Headers:**  
  `Authorization: Bearer <YOUR_JWT_TOKEN>`

- **Response (`200 OK`):**
  ```json
  {
    "success": true,
    "message": "Access granted",
    "data": "This is protected data"
  }
  ```

- **Response (`401 Unauthorized` - Missing or Invalid Token):**
  ```text
  Invalid Token
  ```

- **cURL Example:**
  ```bash
  curl -X GET http://localhost:8083/api/users/profile \
       -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
  ```

---

## 🛡️ Exception Handling & Responses

All application responses and errors adhere to a uniform structure:

```json
{
  "success": false,
  "message": "Invalid email or password",
  "data": null
}
```

Handled via `GlobalExceptionHandler`:
- Custom business logic exceptions (`RuntimeException`)
- Invalid login credentials
- Authorization failures

---

## 🗺️ Future Roadmap

- [ ] **Role-Based Access Control (RBAC)**: Support for `STUDENT`, `FACULTY`, and `ADMIN` roles.
- [ ] **Email Verification**: OTP-based verification on user sign-up.
- [ ] **Campus Feeds & Posts**: Create, comment, and like university announcements and discussion threads.
- [ ] **Peer Connection & Messaging**: Real-time 1-on-1 and group chats using Spring WebSocket & STOMP.
- [ ] **Event Management**: Campus event scheduling, registrations, and RSVP notifications.
- [ ] **Docker & Cloud Deployment**: Dockerfile containerization and CI/CD pipeline via GitHub Actions.

---

## 👨‍💻 Author & Acknowledgments

- **Author**: [Prince Kumar](https://github.com/ydv-prince)
- **Repository**: [ydv-prince/uniconnect](https://github.com/ydv-prince/uniconnect)

---

<div align="center">

*Built with ❤️ for university student communities.*

</div>
