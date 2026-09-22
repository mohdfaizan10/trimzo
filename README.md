# 🔗 Trimzo — URL Shortener & Analytics Backend

A production-grade URL shortening backend service built with Java and Spring Boot.
Similar to bit.ly — but built from scratch with full click analytics.

---

## 🚀 Features

- **URL Shortening** — Base62 encoding algorithm (56 billion unique codes)
- **Click Analytics** — Country, City, Device, Browser, OS, Referrer tracking
- **JWT Authentication** — Secure login with Bearer token
- **API Key System** — Third-party integration via X-API-Key header
- **Geo Detection** — MaxMind GeoLite2 for IP to location lookup
- **Device Detection** — ua-parser for device/browser/OS detection
- **Async Processing** — Click tracking runs in background thread
- **Swagger UI** — Auto-generated API documentation

---

## 🛠️ Tech Stack

| Technology         | Purpose                        |
|--------------------|--------------------------------|
| Java 21            | Primary language               |
| Spring Boot 3.5.14 | Backend framework              |
| Spring Security    | Authentication & Authorization |
| Spring Data JPA    | Database ORM                   |
| PostgreSQL 15      | Primary database               |
| Flyway             | Database migrations            |
| JWT (jjwt 0.12.6)  | Token-based auth               |
| MaxMind GeoLite2   | IP to Country/City             |
| ua-parser          | Device detection               |
| Swagger/OpenAPI    | API documentation              |
| JUnit 5 + H2       | Testing                        |
| Maven              | Build tool                     |

---

## 📋 Prerequisites

- Java 21+
- PostgreSQL 15+
- Maven 3.9+

---

## ⚙️ Setup & Run

### 1. Clone the repository
\```bash
git clone https://github.com/mohdfaizan10/trimzo.git
cd trimzo
\```

### 2. Create PostgreSQL database
\```sql
CREATE DATABASE trimzo_db;
CREATE USER trimzo_user WITH PASSWORD 'your_password';
GRANT ALL PRIVILEGES ON DATABASE trimzo_db TO trimzo_user;
\```

### 3. Configure environment
Update `src/main/resources/application.properties`:
\```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/trimzo_db
spring.datasource.username=trimzo_user
spring.datasource.password=your_password
jwt.secret=yourSecretKeyMinimum32CharactersLong
\```

### 4. Add GeoLite2 database
Download `GeoLite2-City.mmdb` from MaxMind and place it in:
\```
src/main/resources/GeoLite2-City.mmdb
\```

### 5. Run the application
\```bash
mvn spring-boot:run
\```

### 6. Access Swagger UI
\```
http://localhost:8080/swagger-ui/index.html
\```

---

## 🔑 API Overview

### Authentication
| Method | Endpoint              | Description             |
|--------|-----------------------|-------------------------|
| POST   | /api/v1/auth/register | Register new user       |
| POST   | /api/v1/auth/login    | Login and get JWT token |

### URLs
| Method | Endpoint                 | Auth | Description              |
|--------|--------------------------|------|--------------------------|
| POST   | /api/v1/urls             | JWT  | Shorten a URL            |
| GET    | /api/v1/urls             | JWT  | Get all your URLs        |
| DELETE | /api/v1/urls/{shortCode} | JWT  | Delete a URL             |
| GET    | /{shortCode}             | No   | Redirect to original URL |

### Analytics
| Method | Endpoint                           | Auth | Description            |
|--------|------------------------------------|------|------------------------|
| GET    | /api/v1/analytics/{code}/summary   | JWT  | Total & unique clicks  |
| GET    | /api/v1/analytics/{code}/countries | JWT  | Clicks by country/city |
| GET    | /api/v1/analytics/{code}/devices   | JWT  | Clicks by device type  |
| GET    | /api/v1/analytics/{code}/referrers | JWT  | Clicks by referrer     |
| GET    | /api/v1/analytics/{code}/timeline  | JWT  | Clicks per day         |

### API Keys
| Method | Endpoint              | Auth | Description           |
|--------|-----------------------|------|-----------------------|
| POST   | /api/v1/api-keys      | JWT  | Generate new API key  |
| GET    | /api/v1/api-keys      | JWT  | Get all your API keys |
| DELETE | /api/v1/api-keys/{id} | JWT  | Revoke an API key     |

---

## 🧪 Running Tests

\```bash
mvn test
\```

Expected output:
\```
Tests run: 13, Failures: 0, Errors: 0
BUILD SUCCESS
\```

---

## 📁 Project Structure

\```
src/main/java/com/trimzo/
├── config/          # Security, JWT filter, API Key filter
├── controller/      # REST API endpoints
├── service/         # Business logic
├── repository/      # Database operations
├── entity/          # Database table classes
├── dto/             # Request/Response formats
│   ├── request/
│   └── response/
├── exception/       # Custom exceptions
└── util/            # Base62 encoder, parsers
\```

---

## 👨‍💻 Developer

**Mohd Faizan**
- GitHub: [@mohdfaizan10](https://github.com/mohdfaizan10)
- LinkedIn: [mohdfaizan](https://linkedin.com/in/mohdfaizan01)