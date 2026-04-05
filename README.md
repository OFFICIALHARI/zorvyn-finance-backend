# Finance Backend System

Backend assignment submission for a finance dashboard system with JWT authentication, role-based access control, record management, and dashboard summary APIs.

## Overview

This project demonstrates a layered Spring Boot backend designed for clean API structure, secure access control, DTO-based request/response handling, and database-side aggregation for dashboard metrics.

The implementation includes authentication, user management, financial records CRUD, filtering, dashboard summaries, validation, Swagger documentation, and global error handling.

## Tech Stack

- Java 21
- Spring Boot 4
- Spring Security
- Spring Data JPA
- H2 in-memory database
- JWT authentication with jjwt
- Jakarta Validation
- Springdoc OpenAPI / Swagger UI

## Architecture

The project follows a layered architecture:

- Controller layer for HTTP endpoints
- Service layer for business logic
- Repository layer for JPA and aggregation queries
- DTO layer for request and response models
- Security layer for JWT filtering and access control
- Exception layer for global error handling

This keeps controllers thin and avoids exposing JPA entities directly through the API.

## Implemented Features

- User registration and login
- JWT-based stateless authentication
- Role-based access control for VIEWER, ANALYST, and ADMIN
- Active and inactive user handling
- Admin user management APIs
- Financial records CRUD
- Record filtering by date, category, and type
- Dashboard summary APIs using SQL aggregation
- DTO-based API responses
- Input validation and consistent error responses
- Swagger/OpenAPI documentation

## Role Matrix

### VIEWER

- Can view records
- Can view dashboard totals
- Cannot create, update, or delete records
- Cannot manage users

### ANALYST

- Can view records
- Can view dashboard totals
- Cannot create, update, or delete records
- Cannot manage users

### ADMIN

- Can create, update, and delete records
- Can view dashboard totals
- Can list users
- Can update user role
- Can activate or deactivate users

## Authentication Flow

1. Register a user.
2. Login to receive a JWT token.
3. Send the token in the `Authorization` header for secured APIs.

Example:

```text
Authorization: Bearer <your-token>
```

Notes:

- The first registered user is created as `ADMIN`.
- Subsequent users are created as `VIEWER`.

## API Base Path

All endpoints are versioned under:

- `/api/v1`

## API Endpoints

### Auth

- `POST /api/v1/auth/register`
- `POST /api/v1/auth/login`

### Records

- `POST /api/v1/records` - ADMIN only
- `GET /api/v1/records` - VIEWER, ANALYST, ADMIN
- `GET /api/v1/records/{id}` - VIEWER, ANALYST, ADMIN
- `PUT /api/v1/records/{id}` - ADMIN only
- `DELETE /api/v1/records/{id}` - ADMIN only
- `GET /api/v1/records/dashboard` - VIEWER, ANALYST, ADMIN

Query parameters for record filtering:

- `fromDate` in `yyyy-MM-dd`
- `toDate` in `yyyy-MM-dd`
- `category`
- `type` as `INCOME` or `EXPENSE`

### Users

- `GET /api/v1/users` - ADMIN only
- `PATCH /api/v1/users/{id}/role` - ADMIN only
- `PATCH /api/v1/users/{id}/status` - ADMIN only

## Dashboard Logic

Dashboard totals are calculated in the database using JPQL aggregation queries.

This returns:

- Total income
- Total expense
- Net balance

Using aggregation in the repository reduces unnecessary in-memory processing and keeps the service layer focused on business orchestration.

## Validation and Error Handling

Validation is applied using Jakarta Validation annotations such as:

- `@NotBlank`
- `@NotNull`
- `@DecimalMin`
- `@Pattern`
- `@PastOrPresent`

Global exception handling is implemented for:

- validation errors
- access denied errors

This keeps API responses consistent and easier to consume.

## Swagger / API Documentation

Swagger UI:

- `http://localhost:8080/swagger-ui/index.html`

OpenAPI JSON:

- `http://localhost:8080/v3/api-docs`

## How to Run

```bash
./mvnw spring-boot:run
```

Then open:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- H2 Console: `http://localhost:8080/h2-console`

## Demo Testing Flow

1. Register a user at `POST /api/v1/auth/register`
2. Login at `POST /api/v1/auth/login`
3. Copy the returned JWT token
4. Click `Authorize` in Swagger and paste `Bearer <token>`
5. Test dashboard and record endpoints
6. If the user is ADMIN, test user-management endpoints

## Configuration

Important properties:

- `app.jwt.secret`
- `app.jwt.expiration-ms`

For local demo, the JWT secret is set directly in `application.properties`.

## Assumptions and Trade-offs

- H2 is used for fast assessment setup and easy local execution.
- JWT is used for stateless authentication.
- API is intentionally kept simple and readable rather than over-engineered.
- User-scoped access is enforced so users only access their own records.
- Pagination and tests are not included to keep the submission focused and compact.

## Limitations

- No pagination for record listing.
- No automated test suite included.
- H2 is used instead of a production level database.

## Future Improvements

- Pagination for records
- Unit and integration tests
- PostgreSQL profile for production
- Refresh tokens and token revocation
- Better analytics such as trends by month or week
