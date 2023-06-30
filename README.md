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

**Example Request:**

    POST /api/users/insert
    Content-Type: application/json
    
    {
      "name": "John"
      "username": "newuser",
      "email": "newuser@example.com",
      "password": "password123"
    }
    
**Example Response:**
  
    {
      "id": 4,
      "username": "newuser",
      "email": "newuser@example.com"
    }

## User Login (POST /login)

    To authenticate a user and obtain an access token, send a POST request to /login with the user's credentials in the request body.
    
**Example Request:**
    
    POST /login
    Content-Type: application/json
    
    {
      "username": "newuser",
      "password": "password123"
    }
    
**Example Response:**
    
    {
      "access_token": "generated_token_for_newuser"
    }

## Retrieve User Information (GET /api/users/{id})

To retrieve information about a specific user, send a GET request to /api/users/{id} where {id} is the user's identifier.

**Example Request:**

    GET /api/users/4
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
**Example Response:**
    
    {
      "id": 4,
      "username": "exampleuser",
      "email": "exampleuser@example.com"
    }

## Update User Details (PUT /api/users/{id})

To update the details of a specific user, send a PUT request to /api/users/{id} where {id} is the user's identifier. Include the updated user information in the request body.

**Request:**
    
    PUT /api/users/4
    Content-Type: application/json
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
    {
      "username": "updateduser",
      "email": "updateduser@example.com"
    }
    
**Response:**
    
    {
      "id": 1,
      "username": "updateduser",
      "email": "updateduser@example.com"
    }

## Delete a User (DELETE /api/users/{id})

To delete a specific user, send a DELETE request to /api/users/{id} where {id} is the user's identifier.

**Example Request:**

    DELETE /api/users/4
    Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...
    
**Example Response**
    
    HTTP/1.1 204 No Content
    
Please note that the actual request and response formats may differ based on your implementation for this API. Also, all requests for endpoints ccan be done through PowerShell.


### Pagination and Sorting 

### Get All Users with Pagination and Sorting

You can retrieve a paginated and sorted list of all users using the following endpoint:

## GET /api/users/custom

**Query Parameters:**
- `page` (optional): The page number (default: 0).
- `size` (optional): The number of users per page (default: 10).
- `sortField` (optional): The field by which to sort the results (default: "id").
- `sortOrder` (optional): The sorting order, either "asc" (ascending) or "desc" (descending) (default: "asc").

**Example Request:**

GET /api/users/custom?page=0&size=10&sortField=name&sortOrder=asc

**Example Response:**

    {
    "content": [
        {
            "id": 39,
            "refreshToken": "signup_token_for_BobTest1",
            "name": "bob",
            "email": "bob1@example.com",
            "username": "BobTest1",
            "password": "Password679"
        },
        {
            "id": 3,
            "refreshToken": "generated_token_for_bobjones1",
            "name": "Bob Jones",
            "email": "bob.jones@example.com",
            "username": "bobjones1",
            "password": "password789"
        },
        {
            "id": 2,
            "refreshToken": null,
            "name": "Jane Smith",
            "email": "jane.smith@example.com",
            "username": "janes174",
            "password": "password456"
        }
    ],
    "pageable": {
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "offset": 0,
        "pageSize": 10,
        "pageNumber": 0,
        "unpaged": false,
        "paged": true
    },
    "last": true,
    "totalPages": 1,
    "totalElements": 3,
    "size": 10,
    "number": 0,
    "sort": {
        "empty": false,
        "sorted": true,
        "unsorted": false
    },
    "first": true,
    "numberOfElements": 3,
    "empty": false
    }


## Filtering 

Filtering Users (GET /api/users)

To filter users based on specific criteria, send a GET request to /api/users with query parameters to specify the filter field and value.

**Example Request:**
GET /api/users?filterField=name&filterValue=John

**Example Response:**

    [
      {
        "id": 1,
        "name": "John Doe",
        "email": "john.doe@example.com",
        "username": "johndoe1",
        "refresh_token": null
      },
      {
        "id": 3,
        "name": "John Smith",
        "email": "john.smith@example.com",
        "username": "johnsmith",
        "refresh_token": "generated_token_for_johnsmith"
      }
    ]

    
## Input Validation

The API includes input validation. If validation fails, appropriate error responses will be returned to the client.

Please ensure that you provide valid data when creating or updating users to avoid validation errors.Also, you need to make a connection to your MySQL database.  
