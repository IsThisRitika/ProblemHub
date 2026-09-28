Based on https://github.com/mo-farooq/ProblemHub

# Problem Hub

Problem Hub is a Java full-stack web application designed for students and developers to discover, filter, and shortlist real-world problem statements for hackathons, mini projects, major projects, and final-year capstone portfolios.

> **Discover → Understand → Filter → Shortlist → Build**

## Technology Stack

### Backend
- **Java 21**
- **Spring Boot 3.3**
- **Spring Web**
- **Spring JDBC (`JdbcTemplate`)** — *Strictly using JDBC and explicit SQL queries; no JPA/Hibernate*
- **Spring Security + JWT (JSON Web Tokens)**
- **MySQL 8+ / 9+**
- **Maven**

### Frontend
- **Angular 18+** (Standalone Components)
- **TypeScript & SCSS / Tailwind CSS**
- **Angular Router**
- **Angular HttpClient**

---

## Project Structure

```
ProblemHub/
├── backend/
│   ├── src/main/java/com/problemhub/
│   │   ├── controller/
│   │   ├── service/
│   │   ├── repository/
│   │   ├── model/
│   │   ├── dto/
│   │   ├── security/
│   │   ├── exception/
│   │   └── config/
│   └── pom.xml
├── frontend/
│   ├── src/app/
│   │   ├── core/
│   │   ├── shared/
│   │   └── features/
│   └── package.json
└── README.md
```

## Running Locally

### Backend
```bash
cd backend
mvn clean spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm start
```

