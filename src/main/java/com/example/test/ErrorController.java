package com.example.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController, ErrorHandler {

    private final UserRepository userRepository;

    public ErrorController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/error")
    public ResponseEntity<Page<User>> handleError(
            @RequestParam(name = "sortField", defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        // Determine the sorting direction based on the sortOrder parameter
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;

        // Create a Sort object based on the sortField and direction
        Sort sort = Sort.by(direction, sortField);

        // Create a Pageable object for pagination
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<User> usersPage;

        try {
            // Create a Specification object to define filtering conditions
            Specification<User> specification = (root, query, criteriaBuilder) -> {
                // Add your filtering conditions based on the 'filter' parameter
                if (!filter.isEmpty()) {
                    return criteriaBuilder.like(root.get("name"), "%" + filter + "%");
                }
                return null; // Return null if no filtering condition is applied
            };

            // Retrieve the users from the UserRepository based on pagination, sorting, and filtering
            usersPage = userRepository.findAll(specification, pageable);
        } catch (Exception e) {
            // Handle any exceptions that may occur while retrieving the users
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        // Return the paginated list of users in the response body with an OK status
        return ResponseEntity.ok(usersPage);
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
