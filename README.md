Perfect! 😎
Main ab tumhare **Stream Verse backend project ka FINAL, CLEAN, PROFESSIONAL README.md** bana ke de raha hoon, jo **direct GitHub pe copy-paste ke layak** hai.

---

````md
# Stream Verse - Backend

Stream Verse is a Spring Boot backend application for a video streaming platform.  
It provides secure authentication, video and image handling, admin management, and user watchlist features.

---

## Tech Stack

- Java
- Spring Boot
- Spring Security (JWT)
- Spring Data JPA
- Hibernate
- MySQL
- Maven

---

## Features

### Authentication & Security
- User signup with email verification
- JWT-based login
- Resend email verification
- Forgot password and reset password using secure tokens
- Change password for authenticated users
- Role-based authorization (ADMIN / USER)

### User Management (Admin)
- Create and update users
- Paginated user listing with search
- Enable / disable user accounts
- Safe admin role validation

### Video Management
- Video upload with UUID-based storage
- Image upload support
- HTTP Range-based video streaming
- Admin video create, update, delete
- Publish / unpublish videos
- Featured videos and video statistics
- Paginated and searchable video listing

### Watchlist
- Add and remove videos from watchlist
- Paginated watchlist with search support
- Watchlist status included in video responses

### Error Handling
- Centralized exception handling
- Custom exceptions with meaningful responses

---

## Project Structure

```text
src/main/java
└── in.ankit_saahariya.stream_verse
    ├── StreamVerseApplication.java

    ├── config
    │   ├── CorsConfig.java
    │   └── SecurityConfig.java

    ├── controller
    │   ├── AuthController.java
    │   ├── FileUploadController.java
    │   ├── UserController.java
    │   ├── VideoController.java
    │   └── WatchListController.java

    ├── dao
    │   ├── UserRepository.java
    │   └── VideoRepository.java

    ├── dto
    │   ├── request
    │   │   ├── ChangePasswordRequest.java
    │   │   ├── EmailRequest.java
    │   │   ├── LoginRequest.java
    │   │   ├── ResetPasswordRequest.java
    │   │   ├── UserRequest.java
    │   │   └── VideoRequest.java
    │   └── response
    │       ├── EmailValidationResponse.java
    │       ├── ForgotPasswordResponse.java
    │       ├── LoginResponse.java
    │       ├── MessageResponse.java
    │       ├── PageResponse.java
    │       ├── UserResponse.java
    │       ├── VideoResponse.java
    │       └── VideoStatsResponse.java

    ├── entity
    │   ├── UserEntity.java
    │   └── VideoEntity.java

    ├── enums
    │   └── Role.java

    ├── exception
    │   ├── AccountDeactivatedException.java
    │   ├── BadCredentialsException.java
    │   ├── EmailAlreadyExistsException.java
    │   ├── EmailAlreadyVerifiedException.java
    │   ├── EmailNotVerifiedException.java
    │   ├── EmailSendingException.java
    │   ├── InValidCredentialsException.java
    │   ├── InvalidRoleException.java
    │   ├── InvalidTokenException.java
    │   ├── ResourceNotFoundException.java
    │   ├── TokenExpiredException.java
    │   └── GlobalExceptionHandler.java

    ├── security
    │   ├── JwtAuthenticationFilter.java
    │   └── JwtUtil.java

    ├── service
    │   ├── AuthService.java
    │   ├── EmailService.java
    │   ├── FileUploadService.java
    │   ├── UserService.java
    │   ├── VideoService.java
    │   └── WatchListService.java

    ├── serviceImpl
    │   ├── AuthServiceImpl.java
    │   ├── EmailServiceImpl.java
    │   ├── FileUploadServiceImpl.java
    │   ├── UserServiceImpl.java
    │   ├── VideoServiceImpl.java
    │   └── WatchListServiceImpl.java

    └── util
        ├── FileHandlerUtil.java
        ├── PaginationUtils.java
        └── ServiceUtil.java
````

---

## Configuration (`application.properties`)

```properties
# Application
spring.application.name=stream_verse
server.servlet.context-path=/api/v1.1

# Database
spring.datasource.url=jdbc:mysql://localhost:3306/streamverse_db
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect
spring.jpa.properties.hibernate.format_sql=true

# Multipart (Video & Image Upload)
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2GB
spring.servlet.multipart.max-request-size=2GB

server.tomcat.max-swallow-size=-1
server.tomcat.max-http-form-post-size=-1

file.upload.video-dir=uploads/videos
file.upload.image-dir=uploads/images

# Mail Configuration
spring.mail.host=your_smtp_host
spring.mail.port=your_smtp_port
spring.mail.username=your_email
spring.mail.password=your_email_password
spring.mail.protocol=smtp
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true
spring.mail.properties.mail.smtp.connectiontimeout=5000
spring.mail.properties.mail.smtp.timeout=5000
spring.mail.properties.mail.smtp.writetimeout=5000
spring.mail.properties.mail.smtp.from=your_from_email

# Application URLs & Security
app.backend.url=your_backend_url
jwt.secret=your_jwt_secret
```

> Notes:
>
> * Upload directories are automatically created on startup.
> * Video streaming supports HTTP Range requests for playback.
> * Mail configuration is required for signup verification and password reset flows.

---

## How to Run

1. Clone the repository

```bash
git clone <repo-url>
cd stream_verse
```

2. Update `application.properties` with database, mail, and JWT info

3. Run the application

```bash
mvn spring-boot:run
```

4. Access the API

```
http://localhost:8080/api/v1.1
```

---

## API Testing

* Use Postman for testing authentication, file uploads, streaming, and email flows.
* Recommended: capture at least one screenshot for signup/email verification and video streaming.

---

## Author

Ankit Saahariya
Java Backend Developer (Spring Boot)

---


Kya main wo bhi kar du?
```
