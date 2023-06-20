package com.example.test;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/users")
@Validated
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ResponseEntity<String> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Handle validation errors
            StringBuilder errors = new StringBuilder();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.append(error.getField()).append(": ").append(error.getDefaultMessage()).append("; ");
            }
            return ResponseEntity.badRequest().body("Validation errors: " + errors.toString());
        }

        userService.saveUser(user);
        return ResponseEntity.ok("User created successfully");
    }

    // Get a user by their ID
    @GetMapping("/{id}")
    public User getUserById(@PathVariable int id) {
        // Endpoint for retrieving a user by their ID
        return userService.getUserById(id);
    }

    // Get all users with pagination and sorting options
    @GetMapping
    public Page<User> getAllUsers(@RequestParam(defaultValue = "id") String sortField,
                                  @RequestParam(defaultValue = "asc") String sortOrder,
                                  @RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        return userRepository.findAll(pageable);
    }

    // Update a user with a given ID
    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @Valid @RequestBody User user) {
        // Endpoint for updating a user with a given ID
        user.setId(id);
        userService.updateUser(user);
    }

    // Delete a user with a given ID
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        // Endpoint for deleting a user with a given ID
        userService.deleteUser(id);
    }
}
