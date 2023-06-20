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
   

3. Navigate to the project directory:



   cd UserManagementAPI
   

5. Build the application using Maven:



    mvn clean install
   

4.Run the application:


    mvn spring-boot:run

    The application will start on http://localhost:8080.

API Endpoints

The following API endpoints are available:
GET /api/users

This endpoint retrieves all users from the database.

   
   Invoke-WebRequest -Uri "http://localhost:8080/api/users"

   

GET /api/users/{id}

This endpoint retrieves a user by their ID.


   Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}"

   

POST /api/users

This endpoint creates a new user.



   $body = @{
       name = "John Doe"
       email = "john.doe@example.com"
       password = "password123"
   }
   
   Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method Post -Body ($body | ConvertTo-Json) -ContentType "application/json"

   

PUT /api/users/{id}

This endpoint updates an existing user with the specified ID.


   
   $body = @{
       name = "John Doe"
       email = "john.doe@example.com"
       password = "newpassword456"
   }
   
   Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}" -Method Put -Body ($body | ConvertTo-Json) -ContentType "application/json"

   

DELETE /api/users/{id}

This endpoint deletes a user with the specified ID.
   
   Invoke-WebRequest -Uri "http://localhost:8080/api/users/{id}" -Method Delete

   

Pagination

The API supports pagination to retrieve users in smaller chunks. You can specify the page number and page size as query parameters.

Example:


   Invoke-WebRequest -Uri "http://localhost:8080/api/users?page=0&size=10"

   

Filtering

The API allows you to filter users based on specific criteria. You can use query parameters to specify the filter field and value.

Example:


   Invoke-WebRequest -Uri "http://localhost:8080/api/users?filterField=name&filterValue=John"
   

Input Validation

The API includes input validation to ensure data integrity and consistency. The request payloads for creating or updating users are validated to ensure that required fields are present, data types are correct, and any constraints are satisfied.

If validation fails, appropriate error responses will be returned to the client.

Please ensure that you provide valid data when creating or updating users to avoid validation errors.
