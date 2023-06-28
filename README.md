# User Management API

The User Management API is a RESTful web service that provides endpoints for managing user data. It allows users to perform operations such as creating new users, retrieving user information, updating user details, and deleting users.

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

Usage

To use the User Management API, you can send HTTP requests to the provided endpoints using a tool like Postman. The API supports the following HTTP methods:

    GET - Retrieve user information
    POST - Create a new user
    PUT - Update user details
    DELETE - Delete a user

Make sure to include the necessary request headers, request bodies, and authentication tokens as specified in the API documentation for each endpoint.

# Endpoints

## Create a New User (POST /api/users/insert)

To create a new user, send a POST request to /api/users/insert with the user details in the request body.

    Request

    POST /api/users/insert
    Content-Type: application/json
    
    {
      "name": "John"
      "username": "newuser",
      "email": "newuser@example.com",
      "password": "password123"
    }
    
    Response
  
    {
      "id": 4,
      "username": "newuser",
      "email": "newuser@example.com"
    }

## User Login (POST /login)

    To authenticate a user and obtain an access token, send a POST request to /login with the user's credentials in the request body.
    
    Request
    
    POST /login
    Content-Type: application/json
    
    {
      "username": "newuser",
      "password": "password123"
    }
    
    Response
    
    {
      "access_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
    }

## Retrieve User Information (GET /api/users/{id})

To retrieve information about a specific user, send a GET request to /api/users/{id} where {id} is the user's identifier.
Request

    GET /api/users/4
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
    Response
    
    {
      "id": 4,
      "username": "exampleuser",
      "email": "exampleuser@example.com"
    }

## Update User Details (PUT /api/users/{id})

To update the details of a specific user, send a PUT request to /api/users/{id} where {id} is the user's identifier. Include the updated user information in the request body.

    Request
    
    PUT /api/users/4
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
    {
      "username": "updateduser",
      "email": "updateduser@example.com"
    }
    
    Response
    
    {
      "id": 1,
      "username": "updateduser",
      "email": "updateduser@example.com"
    }

## Delete a User (DELETE /api/users/{id})

To delete a specific user, send a DELETE request to /api/users/{id} where {id} is the user's identifier.
Request

    DELETE /api/users/4
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
    Response
    
    HTTP/1.1 204 No Content
    
Please note that the actual request and response formats may differ based on your implementation for this API. Also, all requests for endpoints ccan be done through PowerShell.


## Pagination

## GET /api/users: Retrieves users with pagination. Specify the page number and page size as query parameters.

    Invoke-WebRequest -Uri "http://localhost:8080/api/users?page=0&size=10"

## Sorting 

## GET /api/users: Sorts users based on specific criteria. Use query parameters to specify the sort field and order.

    Invoke-WebRequest -Uri "http://localhost:8080/api/users?sortField=name&sortOrder"

##Filtering 

## GET /api/users: Filters users based on specific criteria. Use query parameters to specify the filter field and value.

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
