# EduAnalysis API

A comprehensive REST API for managing the International Educational Analysis Registry. This application provides endpoints for managing analysts, users, organizational data, and supports secure authentication with JWT tokens.

## Features

- **User Management**: Create, update, and manage user accounts with role-based access control
- **Analyst Management**: Handle analyst profiles including personal information, firm associations, and document attachments
- **Authentication & Authorization**: JWT-based authentication with role and capability-based permissions
- **File Upload**: Support for uploading and managing identity documents and attachments
- **Database Migrations**: Automated schema management using Flyway
- **API Documentation**: Interactive Swagger UI documentation
- **Security**: Spring Security integration with bcrypt password hashing
- **Validation**: Comprehensive input validation using Bean Validation
- **Auditing**: JPA auditing for tracking entity changes
- **Async Processing**: Support for asynchronous operations
- **Retry Mechanism**: Configurable retry logic for resilient operations

## Architecture

The application follows a layered architecture:

- **API Layer**: REST controllers handling HTTP requests (`api` package)
- **Service Layer**: Business logic implementation (`service` package)
- **Repository Layer**: Data access using Spring Data JPA (`repository` package)
- **Model Layer**: JPA entities representing domain objects (`model` package)
- **DTO Layer**: Data Transfer Objects for API communication (`dto` package)
- **Security Layer**: Authentication and authorization components (`security`, `authentication` packages)
- **Validation Layer**: Custom validators for business rules (`validator` package)
- **Specification Layer**: Dynamic query specifications (`specification` package)

## Technology Stack

- **Framework**: Spring Boot 3.5.13
- **Language**: Java 21
- **Build Tool**: Gradle
- **Database**: PostgreSQL
- **ORM**: Hibernate/JPA
- **Migration**: Flyway
- **Security**: Spring Security + JWT
- **Documentation**: OpenAPI/Swagger
- **Validation**: Hibernate Validator
- **File Processing**: Apache Tika
- **Utilities**: Lombok, Apache Commons Lang

## Prerequisites

- Java 21 or higher
- Gradle 7+ (or use the included Gradle wrapper)
- PostgreSQL 12+ database server

## Setup

### Database Setup

1. Install and start PostgreSQL server
2. Create a database named `edu-analysis-db` (or configure via `PG_DB` environment variable)
3. The application uses Flyway for automatic schema migration

### Environment Variables

Set the following environment variables:

```bash
# Database Configuration
DB_USERNAME=your_db_username
DB_PASSWORD=your_db_password
PG_HOST=localhost          # Optional, default: localhost
PG_PORT=5433              # Optional, default: 5433
PG_DB=edu-analysis-db     # Optional, default: edu-analysis-db

# JWT Configuration
JWT_SECRET_KEY=your_256_bit_secret_key_here
JWT_EXPIRATION=10800000  # Optional, default: 10800000 (3 hours in ms)

# File Upload (Optional)
FILE_UPLOAD_DIR=uploads/  # Optional, default: uploads/
```

### Generate JWT Secret Key

You can generate a secure 256-bit secret key using:

```bash
openssl rand -hex 32
```

## Build

### Using Gradle Wrapper (Recommended)

```bash
# Build the application
./gradlew build

# On Windows
gradlew.bat build
```

### Using System Gradle

```bash
gradle build
```

## Run

### Development Mode

```bash
# Run with Gradle wrapper
./gradlew bootRun

# On Windows
gradlew.bat bootRun
```

The application will start on `http://localhost:8080` with the `dev` profile active.

### Production Mode

1. Build the JAR:
   ```bash
   ./gradlew bootJar
   ```

2. Run the JAR:
   ```bash
   java -jar build/libs/edu-analysis-app-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod
   ```

## API Endpoints

### Authentication
- `POST /api/auth/authenticate` - Login and obtain JWT token

### User Management
- `GET /api/users` - List users
- `POST /api/users` - Create user
- `GET /api/users/{id}` - Get user details
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Analyst Management
- `GET /api/analysts` - List analysts
- `POST /api/analysts` - Create analyst
- `GET /api/analysts/{id}` - Get analyst details
- `PUT /api/analysts/{id}` - Update analyst
- `DELETE /api/analysts/{id}` - Delete analyst

### Additional Endpoints
- `GET /api/eligible` - Check eligibility (specific business logic)

## API Documentation

Once the application is running, access the Swagger UI at:
`http://localhost:8080/swagger-ui.html`

The OpenAPI specification is available at:
`http://localhost:8080/v3/api-docs`

## Testing

### Run Unit Tests

```bash
./gradlew test
```

### Run Integration Tests

```bash
./gradlew integrationTest  # If configured
```

### Test Coverage

```bash
./gradlew test jacocoTestReport
```

Reports will be generated in `build/reports/jacoco/test/html/index.html`

## Configuration

### Application Profiles

- `dev` (default): Development configuration with debug logging
- `prod`: Production configuration

### Key Configuration Files

- `application.properties` - Common configuration
- `application-dev.properties` - Development-specific settings
- `application-prod.properties` - Production-specific settings (create if needed)

### Logging

Logs are configured via `logback-spring.xml`. Log files are written to the `logs/` directory.

## Database Schema

The application uses Flyway migrations located in `src/main/resources/db/migration/`:

- `V1__initial_schema.sql` - Core tables (users, roles, analysts, etc.)
- `V2__insert_firms.sql` - Initial firm data
- `V3__insert_roles_capabilites.sql` - Roles and capabilities setup
- `V4__insert_view_user_capability_to_admin.sql` - Admin view permissions

## Security

- JWT tokens are required for authenticated endpoints
- Passwords are hashed using BCrypt with strength 12
- Role-based access control with capabilities
- CORS configured for specified origins

## File Upload

- Files are stored in the `uploads/` directory
- Supported for identity documents and attachments
- Maximum file size: 5MB per file, 5MB per request

## Development

### Code Style

Follow standard Java conventions and Spring Boot best practices.

### Adding New Features

1. Create DTOs in the `dto` package
2. Implement validators in the `validator` package
3. Add service logic in the `service` package
4. Create repository interfaces in the `repository` package
5. Add REST endpoints in the `api` package

### Database Changes

1. Create new migration files in `src/main/resources/db/migration/`
2. Follow the naming convention: `V{number}__{description}.sql`

## Deployment

### Docker (Example)

```dockerfile
FROM openjdk:21-jdk-slim
COPY build/libs/edu-analysis-app-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
```

### Build Docker Image

```bash
./gradlew bootBuildImage
```

## Troubleshooting

### Common Issues

1. **Database Connection Failed**
   - Verify PostgreSQL is running
   - Check environment variables
   - Ensure database exists

2. **JWT Authentication Issues**
   - Verify `JWT_SECRET_KEY` is set
   - Check token expiration

3. **File Upload Errors**
   - Check `uploads/` directory permissions
   - Verify file size limits

### Logs

Check application logs in `logs/` directory for detailed error information.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

This project is licensed under CC0 1.0 Universal - see the [license](https://creativecommons.org/publicdomain/zero/1.0) for details.

## Contact

- **Author**: PanTs @ CF
- **Email**: pants.ath@gmail.com
- **Website**: https://panos1924t.github.io/personal-cv/

## Version

Current Version: 0.0.1-SNAPSHOT