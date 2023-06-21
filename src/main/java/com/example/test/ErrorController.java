package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController, ErrorHandler {

    private final UserService userService;

    @Autowired
    public ErrorController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/error")
    public ResponseEntity<Page<User>> handleError() {
        // Define the sorting and paging parameters
        String sortField = "id";
        String sortOrder = "asc";
        int page = 0;
        int size = 10;

        // Create the sorting object
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);

        // Create the paging object
        Pageable pageable = PageRequest.of(page, size, sort);

        // Get the users from the service with pagination and sorting
        Page<User> userPage = userService.getAllUsers("", pageable);

        // Return the users in the response body with an OK status
        return ResponseEntity.ok(userPage);
    }

    // Implement the getErrorPath() method required by the ErrorController interface
    @Override
    public String getErrorPath() {
        return "/error";
    }
}
