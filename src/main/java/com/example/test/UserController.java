package com.example.test;


import com.example.test.errors.PasswordPatternValidationException;
import com.example.test.errors.UsernamePatternValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/", "/api", "/api/users", "/api/users/insert"})

public class UserController {

    private final UserService userService;

    // HashMap to store the tokens for users
    private final Map<String, String> tokenMap = new HashMap<>();

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Object> handleInvalidCredentialsException(InvalidCredentialsException ex) {
        // Create a custom error response for InvalidCredentialsException
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("error", "Invalid credentials");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
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

        // Generate a token
        String token = signUpToken(user.getUsername());

        // Set the token in the user entity
        user.setRefreshToken(token);

        // Save the user
        userService.saveUser(user);

        // Create the response object that includes the token
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        // Create the response headers and set the token
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + token);

        // Return the response with headers and a success message
        return ResponseEntity.ok().headers(headers).body(response);
    }

    // Utility method to generate a token
    private String signUpToken(String username) {
        // Implement your token generation logic here
        // For simplicity, we'll just use a basic token format
        return "signup_token_for_" + username;
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


    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid User user, BindingResult bindingResult) throws InvalidCredentialsException {
        if (bindingResult.hasErrors()) {
            // Collect all field validation errors
            List<String> errors = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.add(error.getDefaultMessage());
            }

            // Return the validation errors as a JSON response with a bad request status
            return ResponseEntity.badRequest().body(errors);
        }

        // Perform login authentication using a custom query
        boolean isValidCredentials = userService.customLogin(user.getUsername(), user.getPassword());

        if (!isValidCredentials) {
            throw new InvalidCredentialsException("Invalid username or password.");
        }

        // Generate a token for successful login
        String token = generateToken(user.getUsername());

        // Retrieve the user from the database
        User authenticatedUser = userService.getUserByUsername(user.getUsername());

        // Save the token to the user entity
        authenticatedUser.setRefreshToken(token);
        userService.updateUser(authenticatedUser);

        // Create a response object that includes the token
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);

        return ResponseEntity.ok(response);
    }

    // Utility method to generate a token
    private String generateToken(String username) {
        // Implement your token generation logic here
        // This is just a placeholder implementation
        return "generated_token_for_" + username;
    }


}

