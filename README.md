# Task Management System API

## 1. Giới thiệu

Task Management System là hệ thống **RESTful API** được xây dựng bằng **Spring Boot** nhằm hỗ trợ quản lý công việc trong dự án.

Hệ thống cung cấp các chức năng chính:

* Quản lý người dùng (User Management)
* Quản lý dự án (Project Management)
* Quản lý công việc (Task Management)
* Phân công công việc cho người dùng
* Xác thực người dùng bằng **JWT Authentication**

API được tài liệu hóa bằng **Swagger/OpenAPI** để dễ dàng kiểm thử và sử dụng.

Project được phát triển nhằm phục vụ **mục đích học tập và thực tập Backend Java với Spring Boot**.

---

# 2. Công nghệ sử dụng

| Công nghệ         | Mô tả                     |
| ----------------- | ------------------------- |
| Java 17           | Ngôn ngữ lập trình        |
| Spring Boot       | Framework backend         |
| Spring Security   | Bảo mật hệ thống          |
| JWT               | Xác thực người dùng       |
| Spring Data JPA   | ORM truy cập database     |
| MySQL             | Hệ quản trị cơ sở dữ liệu |
| Swagger (OpenAPI) | Tài liệu API              |
| Maven             | Quản lý dependencies      |

---

# 3. Cấu trúc project

Dưới đây là cấu trúc thư mục chính của project:

```
src
 ├── main
 │   ├── java/com/example/demo
 │   │   ├── config        # Cấu hình hệ thống
 │   │   ├── controller    # REST API controllers
 │   │   ├── dto           # Data Transfer Objects
 │   │   ├── entity        # Entity classes (JPA)
 │   │   ├── exception     # Xử lý exception
 │   │   ├── repository    # JPA repositories
 │   │   ├── security      # JWT & Spring Security
 │   │   ├── service       # Business logic
 │   │   ├── validation    # Validation dữ liệu
 │   │   └── DemoApplication.java
 │   │
 │   └── resources
 │       ├── static
 │       ├── templates
 │       ├── application.properties
 │       ├── application-dev.properties
 │       └── application-prod.properties
 │
 └── test
     └── java/com/example/demo
         ├── DemoApplicationTests
         ├── ProjectMappingTest
         ├── TaskMappingTest
         └── TaskServiceTest
```

---

# 4. Cài đặt hệ thống (Setup)

## 4.1 Clone project

```
git clone https://github.com/your-username/task-management-api.git
```

Di chuyển vào thư mục project

```
cd task-management-api
```

---

# 5. Cấu hình Database

Tạo database trong MySQL:

```
CREATE DATABASE task_management;
```

Mở file:

```
src/main/resources/application.properties
```

Cấu hình kết nối database:

```
spring.datasource.url=jdbc:mysql://localhost:3306/task_management
spring.datasource.username=root
spring.datasource.password=123456

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

---

# 6. Chạy project (Run)

Chạy project bằng Maven:

```
mvn spring-boot:run
```

Hoặc build project:

```
mvn clean package
```

Sau đó chạy file jar:

```
java -jar target/demo.jar
```

Server sẽ chạy tại:

```
http://localhost:8080
```

---

# 7. Swagger API Documentation

Swagger UI dùng để xem và kiểm thử API.

Truy cập:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger hiển thị các nhóm API:

* Authentication API
* User API
* Project API
* Task API

Người dùng có thể:

* Xem endpoint
* Xem request / response
* Test API trực tiếp

---

# 8. Authentication (JWT)

Hệ thống sử dụng **JWT Token** để bảo vệ các API.

Các API cần token:

* User API
* Project API
* Task API

---

# 9. Quy trình xác thực

## Bước 1: Đăng ký tài khoản

Endpoint

```
POST /api/auth/register
```

Request

```json
{
  "username": "admin",
  "password": "123456",
  "email": "admin@gmail.com"
}
```

---

## Bước 2: Đăng nhập

Endpoint

```
POST /api/auth/login
```

Request

```json
{
  "username": "admin",
  "password": "123456"
}
```

Response

```
eyJhbGciOiJIUzI1NiJ9...
```

Đây là **JWT Token**.

---

## Bước 3: Sử dụng Token

Khi gọi các API cần thêm header:

```
Authorization: Bearer <token>
```

Ví dụ

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

# 10. Danh sách API

## Authentication API

| Method | Endpoint           | Description       |
| ------ | ------------------ | ----------------- |
| POST   | /api/auth/register | Đăng ký tài khoản |
| POST   | /api/auth/login    | Đăng nhập         |

---

## User API

| Method | Endpoint               | Description              |
| ------ | ---------------------- | ------------------------ |
| GET    | /api/users             | Lấy danh sách người dùng |
| GET    | /api/users/{id}        | Lấy user theo ID         |
| POST   | /api/users             | Tạo user                 |
| PUT    | /api/users/{id}/status | Cập nhật trạng thái      |
| DELETE | /api/users/{id}        | Xóa user                 |

---

## Project API

| Method | Endpoint           | Description           |
| ------ | ------------------ | --------------------- |
| GET    | /api/projects      | Lấy danh sách project |
| GET    | /api/projects/{id} | Lấy project theo ID   |
| POST   | /api/projects      | Tạo project           |
| PUT    | /api/projects/{id} | Cập nhật project      |
| DELETE | /api/projects/{id} | Xóa project           |

---

## Task API

| Method | Endpoint                       | Description         |
| ------ | ------------------------------ | ------------------- |
| GET    | /api/tasks                     | Lấy tất cả task     |
| GET    | /api/tasks/{id}                | Lấy task theo ID    |
| POST   | /api/tasks                     | Tạo task            |
| PUT    | /api/tasks/{id}                | Cập nhật task       |
| DELETE | /api/tasks/{id}                | Xóa task            |
| GET    | /api/tasks/user/{userId}       | Task theo user      |
| GET    | /api/tasks/project/{projectId} | Task theo project   |
| PUT    | /api/tasks/{id}/assign         | Gán task            |
| PUT    | /api/tasks/{id}/status         | Cập nhật trạng thái |

---

# 11. Kiểm thử API

API có thể được kiểm thử bằng:

* Swagger UI
* Postman

Ví dụ:

```
GET /api/projects
```

Header

```
Authorization: Bearer <token>
```

Response trả về **HTTP 200 OK** cùng dữ liệu JSON.

---

# 12. Mục tiêu project

Project được xây dựng nhằm:

* Thực hành xây dựng RESTful API với Spring Boot
* Áp dụng Spring Security và JWT
* Sử dụng Swagger để document API
* Thực hành Spring Data JPA với MySQL

---

# 13. Tác giả

Sinh viên thực tập 

Project phục vụ mục đích **học tập và thực hành Spring Boot**.

# 14. Hướng dẫn Test API

Project có thể được kiểm thử bằng **Swagger UI** hoặc **Postman**.

---

# 14.1 Test bằng Swagger

Sau khi chạy project, truy cập Swagger UI:

```
http://localhost:8080/swagger-ui/index.html
```

Swagger sẽ hiển thị danh sách các API:

* Auth API
* User API
* Project API
* Task API

---

## Bước 1: Đăng ký tài khoản

Chọn API:

```
POST /api/auth/register
```

Request body:

```json
{
  "username": "admin",
  "password": "123456",
  "email": "admin@gmail.com"
}
```

Nhấn **Execute** để tạo tài khoản.

---

## Bước 2: Đăng nhập

Chọn API:

```
POST /api/auth/login
```

Request body:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Response trả về:

```
JWT Token
```

Ví dụ:

```
eyJhbGciOiJIUzI1NiJ9...
```

---

## Bước 3: Thêm Token vào Swagger

Ở góc trên bên phải Swagger UI, chọn **Authorize**.

Nhập token theo format:

```
Bearer <token>
```

Ví dụ:

```
Bearer eyJhbGciOiJIUzI1NiJ9...
```

Sau đó nhấn **Authorize**.

---

## Bước 4: Test các API

Sau khi authorize thành công, bạn có thể test:

### User API

```
GET /api/users
POST /api/users
DELETE /api/users/{id}
```

### Project API

```
GET /api/projects
POST /api/projects
PUT /api/projects/{id}
DELETE /api/projects/{id}
```

### Task API

```
GET /api/tasks
POST /api/tasks
PUT /api/tasks/{id}
DELETE /api/tasks/{id}
```

---

# 14.2 Test bằng Postman

## Bước 1: Login để lấy token

Request:

```
POST http://localhost:8080/api/auth/login
```

Body:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Response:

```
JWT Token
```

---

## Bước 2: Thêm Header Authorization

Khi gọi API cần thêm header:

```
Authorization: Bearer <token>
```

Ví dụ:

```
Authorization: Bearer eyJhbGciOiJIUzI1NiJ9...
```

---

## Ví dụ Test API

### Lấy danh sách project

```
GET http://localhost:8080/api/projects
```

Header:

```
Authorization: Bearer <token>
```

Response:

```
HTTP 200 OK
```

---

# 14.3 Các lỗi thường gặp

### 403 Forbidden

Nguyên nhân:

* Chưa đăng nhập
* Chưa thêm JWT Token
* Token sai format

Cách khắc phục:

```
Authorization: Bearer <token>
```

---

### 401 Unauthorized

Nguyên nhân:

* Token hết hạn
* Token không hợp lệ

Cách khắc phục:

Đăng nhập lại để lấy token mới.
