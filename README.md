UserManagementAPI

This is a sample Spring Boot application that provides an API for managing user data.
Prerequisites

    Java Development Kit (JDK) 8 or later
    Maven

Getting Started

    Clone the repository:

bash

git clone https://github.com/YourUsername/UserManagementAPI.git

    Navigate to the project directory:

bash

cd UserManagementAPI

    Build the application using Maven:

bash

mvn clean install

    Run the application:

bash

mvn spring-boot:run

The application will start on http://localhost:8080.
API Endpoints

The following API endpoints are available:

    GET /api/users: Retrieve all users.
    GET /api/users/{id}: Retrieve a user by their ID.
    POST /api/users: Create a new user.
    PUT /api/users/{id}: Update an existing user.
    DELETE /api/users/{id}: Delete a user.

GET /api/users

This endpoint retrieves all users from the database.

Example request using PowerShell:

powershell

Invoke-WebRequest -Uri "http://localhost:8080/api/users"

POST /api/users

This endpoint creates a new user.

Example request using PowerShell:

$body = @{
    name = "John Doe"
    email = "john.doe@example.com"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/users" -Method Post -Body ($body | ConvertTo-Json) -ContentType "application/json"

PUT /api/users/{id}

This endpoint updates an existing user with the specified ID.

Example request using PowerShell:


$body = @{
    name = "John Doe"
    email = "john.doe@example.com"
}

Invoke-WebRequest -Uri "http://localhost:8080/api/users/1" -Method Put -Body ($body | ConvertTo-Json) -ContentType "application/json"

DELETE /api/users/{id}

This endpoint deletes a user with the specified ID.

Example request using PowerShell:

powershell

Invoke-WebRequest -Uri "http://localhost:8080/api/users/1" -Method Delete
