package com.example.test;


import com.example.test.errors.PasswordPatternValidationException;
import com.example.test.errors.UsernamePatternValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
@RestController
@RequestMapping({"/", "/api", "/api/users", "/api/users/insert"})

public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Create a new user.
     *
     * @param user The user object to be created
     */
    @Autowired
    private JwtTokenUtil jwtTokenUtil;



    @PostMapping("/insert")
    public ResponseEntity<Object> createUser(@RequestBody @Valid User user, BindingResult bindingResult) throws PasswordPatternValidationException, UsernamePatternValidationException {
        if (bindingResult.hasErrors()) {
            // Collect all field validation errors
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }

            // Return the validation errors as a JSON response with a bad request status
            return ResponseEntity.badRequest().body(errors);
        }

        // Validate the username and password using the custom regular expression pattern
        if (!user.getUsername().matches("^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,50}$")) {
            throw new UsernamePatternValidationException("Invalid username format. It must contain at least one lowercase letter, one uppercase letter, one digit, and be between 8 and 50 characters long.");
        }

        if (!user.getPassword().matches("^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,50}$")) {
            throw new PasswordPatternValidationException("Invalid password format. It must contain at least one lowercase letter, one uppercase letter, one digit, and be between 8 and 50 characters long.");
        }

        // Save the user and generate a token
        userService.saveUser(user);
        String token = jwtTokenUtil.generateToken(user.getUsername());
        user.setRefreshToken(token);

        return ResponseEntity.ok("User created successfully");
    }



    /**
     * Get a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The response entity containing the user if found, or a "Not Found" response otherwise
     */
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable int id) {
        User user = userService.getUserById(id);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(user);
    }

    /**
     * Get all users with pagination, sorting, and filtering options.
     *
     * @param sortField The field to sort the users by (default: "id")
     * @param sortOrder The sort order ("asc" for ascending, "desc" for descending; default: "asc")
     * @param filter    The filter to apply to the username (default: "")
     * @param page      The page number (default: 0)
     * @param size      The number of users per page (default: 10)
     * @return The response entity containing the paginated list of users
     */
    @GetMapping
    public ResponseEntity<Page<User>> getAllUsers(
            @RequestParam(defaultValue = "id") String sortField,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "") String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Sort.Direction direction = sortOrder.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, sortField);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<User> usersPage = userService.getAllUsers(filter, pageable);
        return ResponseEntity.ok(usersPage);
    }

    /**
     * Update an existing user.
     *
     * @param id   The ID of the user to update
     * @param user The updated user object
     */
    @PutMapping("/{id}")
    public void updateUser(@PathVariable int id, @RequestBody User user) {
        user.setId(id);
        userService.updateUser(user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to delete
     */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
    }
}

