
# Stream Verse - Backend

**Stream Verse** is a Spring Boot backend application for a video streaming platform.
It provides secure authentication, video & image handling, admin management, and user watchlist features.

---

## 🚀 Tech Stack

- **Java** (JDK 17+)
- **Spring Boot**
- **Spring Security (JWT)**
- **Spring Data JPA**
- **Hibernate**
- **MySQL**
- **Maven**

---

## ✨ Features

### Authentication & Security
- User signup with email verification
- JWT-based login
- Resend email verification
- Forgot password & reset password using secure tokens
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
- HTTP Range-based video streaming (smooth playback)
- Admin video create, update, delete
- Publish / unpublish videos
- Featured videos & video statistics
- Paginated & searchable video listing

### Watchlist
- Add/remove videos from watchlist
- Paginated watchlist with search support
- Watchlist status included in video responses

### Error Handling
- Centralized exception handling
- Custom exceptions with meaningful responses

---

## 📂 Project Structure


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
    │   │   ├── LoginRequest.java
    │   │   ├── UserRequest.java
    │   │   └── VideoRequest.java
    │   └── response
    │       ├── LoginResponse.java
    │       ├── UserResponse.java
    │       └── VideoResponse.java
    ├── entity
    │   ├── UserEntity.java
    │   └── VideoEntity.java
    ├── exception
    │   └── GlobalExceptionHandler.java
    ├── security
    │   ├── JwtAuthenticationFilter.java
    │   └── JwtUtil.java
    ├── service
    │   ├── AuthService.java
    │   ├── UserService.java
    │   └── VideoService.java
    └── util
        └── FileHandlerUtil.java

```

---

## ▶️ How to Run

1. **Clone the repository**
```bash
git clone [https://github.com/your-username/stream-verse.git](https://github.com/your-username/stream-verse.git)
cd stream_verse

```


2. **Configure Database**
Make sure your MySQL server is running and the credentials in `application.properties` are correct.
3. **Build and Run**
```bash
mvn spring-boot:run

```
4. **Access the API**
The server will start at: `http://localhost:8080/api/v1.1`

---



## 🛠️ Prerequisites

Before running this application, ensure you have the following installed:

* **Java Development Kit (JDK) 17** or higher
* **Maven 3.x**
* **MySQL Server 8.0**
* **Postman** (for testing APIs)

---

## ⚙️ Configuration

Update `src/main/resources/application.properties` with your credentials:

```properties
# Application
spring.application.name=stream_verse
server.servlet.context-path=/api/v1.1

# Database Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/streamverse_db?createDatabaseIfNotExist=true
spring.datasource.username=your_db_username
spring.datasource.password=your_db_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# File Upload (Multipart)
spring.servlet.multipart.enabled=true
spring.servlet.multipart.max-file-size=2GB
spring.servlet.multipart.max-request-size=2GB
file.upload.video-dir=uploads/videos
file.upload.image-dir=uploads/images

# Mail Configuration (SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your_email@gmail.com
spring.mail.password=your_app_password
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# JWT Security
jwt.secret=your_super_secret_jwt_key_here

```





## 🔌 API Documentation

### 1. Authentication (`AuthController`)
*Base URL: `/api/v1.1/auth`*

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| POST | `/signup` | Register a new user | ❌ |
| POST | `/login` | Login user and generate JWT token | ❌ |
| POST | `/resend/verification` | Resend email verification link | ❌ |
| GET | `/validate-email` | Validate email availability | ❌ |
| POST | `/forgot-password` | Send password reset email | ❌ |
| POST | `/reset-password` | Reset password using token | ❌ |
| POST | `/change-password` | Change password (logged-in user) | ✅ |
| GET | `/current-user` | Get current authenticated user details | ✅ |

### 2. User Management (`UserController`)
*Base URL: `/api/v1.1/users`* **Note:** These endpoints are restricted to **ADMIN** role only.

| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/` | Create a new user |
| GET | `/` | Get paginated list of users |
| PUT | `/{id}` | Update user details |
| DELETE | `/{id}` | Delete user |
| PUT | `/{id}/toggle-status` | Enable or disable user account |
| PUT | `/{id}/change-role` | Change user role (USER/ADMIN) |

### 3. File Management (`FileController`)
*Base URL: `/api/v1.1/files`*

| Method | Endpoint | Description | Auth (Admin) |
| :--- | :--- | :--- | :---: |
| POST | `/upload/video` | Upload video file | ✅ |
| POST | `/upload/image` | Upload image/poster | ✅ |
| GET | `/video/{uuid}` | **Stream video** (Supports Range Headers) | ❌ |
| GET | `/image/{uuid}` | Serve image by UUID | ❌ |

### 4. Video Management (`VideoController`)
*Base URL: `/api/v1.1/videos`*

#### Admin Operations
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| POST | `/admin` | Create new video entry |
| GET | `/admin` | Fetch all videos (Admin view) |
| PUT | `/admin/{id}` | Update video details |
| DELETE | `/admin/{id}` | Delete video |
| PATCH | `/admin/{id}/publish` | Publish / Unpublish video (`?value=true/false`) |
| GET | `/admin/stats` | Get system video statistics |

#### Public / User Operations
| Method | Endpoint | Description |
| :--- | :--- | :--- |
| GET | `/published` | Get all published videos (Pagination supported) |
| GET | `/featured` | Get list of featured videos |

### 5. Watchlist (`WatchListController`)
*Base URL: `/api/v1.1/watchList`*

| Method | Endpoint | Description | Auth Required |
| :--- | :--- | :--- | :---: |
| GET | `/` | Get user watchlist (Paginated) | ✅ |
| POST | `/{videoId}` | Add video to watchlist | ✅ |
| DELETE | `/{videoId}` | Remove video from watchlist | ✅ |



## Postman Collection
This project includes a Postman collection JSON file containing all API endpoints.
It can be imported into Postman to test and understand the API flow.


---

## 👤 Author

**Ankit Saahariya**

* **Role:** Java Backend Developer
* **LinkedIn:** [Link to your LinkedIn Profile](https://www.google.com/search?q=https://linkedin.com/in/your-profile)
* **GitHub:** [Link to your GitHub Profile](https://www.google.com/search?q=https://github.com/your-username)

---

## 📜 License

This project is licensed under the MIT License - see the [LICENSE](https://www.google.com/search?q=LICENSE) file for details.

```

