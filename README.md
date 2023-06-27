# User Management API

This is a sample Spring Boot application that provides an API for managing user data.

## Prerequisites

- Java Development Kit (JDK) 8 or later
- Maven
- MySQL
- IntelliJ IDEA

## Getting Started

1. Clone the repository:

git clone https://github.com/YourUsername/UserManagementAPI.git

2. Navigate to the project directory:

cd UserManagementAPI

3. Build the application using Maven:

mvn clean install

4. Run the application:

mvn spring-boot:run

The application will start on http://localhost:8080.

## API Endpoints

## GET /api/users: Retrieves all users from the database.

Invoke-WebRequest -Uri "http://localhost:8080/api/users"

## GET /api/users/{id}: Retrieves a user by their ID.

Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}"

## Create
POST /api/users: Creates a new user.

$body = @{
    name = "John Doe"
    email = "john.doe@example.com"
    password = "password123"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method Post -Body ($body | ConvertTo-Json) -ContentType "application/json"

## Update 

PUT /api/users/{id}: Updates an existing user with the specified ID.

$body = @{
    name = "John Doe"
    email = "john.doe@example.com"
    password = "newpassword456"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}" -Method Put -Body ($body | ConvertTo-Json) -ContentType "application/json"

## Delete

DELETE /api/users/{id}: Deletes a user with the specified ID.

Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}" -Method Delete

## Pagination

GET /api/users: Retrieves users with pagination. Specify the page number and page size as query parameters.

Invoke-WebRequest -Uri "http://localhost:8080/api/users?page=0&size=10"

## Sorting 

GET /api/users: Sorts users based on specific criteria. Use query parameters to specify the sort field and order.

Invoke-WebRequest -Uri "http://localhost:8080/api/users?sortField=name&sortOrder"

##Filtering 

GET /api/users: Filters users based on specific criteria. Use query parameters to specify the filter field and value.

Invoke-WebRequest -Uri "http://localhost:8080/api/users?filterField=name&filterValue=John"

## POST /api/users/insert: Creates a new user.

$body = @{
    name = "John Doe"
    email = "john.doe@example.com"
    password = "password123"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/users/insert" -Method Post
## Input Validation

The API includes input validation. If validation fails, appropriate error responses will be returned to the client.

Please ensure that you provide valid data when creating or updating users to avoid validation errors.Also, you need to make a connection to your MySQL database.  
