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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.security.SecureRandom;
import java.util.*;

@RestController
@RequestMapping({"/", "/api", "/api/users", "/api/users/insert","api/users/forgot-password"})

public class UserController {

    private final UserService userService;

    // HashMap to store the tokens for users
    private final Map<String, String> tokenMap = new HashMap<>();


    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";


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



    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("api/users/insert")
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

        // Generate a signup token
        String signUpToken = generateSignUpToken(user.getUsername());

        // Set the token in the user entity
        user.setRefreshToken(signUpToken);

        // Save the user
        userService.saveUser(user);

        // Create the response object that includes the token
        Map<String, Object> response = new HashMap<>();
        response.put("token", signUpToken);

        // Set a custom message in the token
        response.put("message", "User created successfully.");

        // Create the response headers and set the token with the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + signUpToken);

        // Return the response with headers and a success message
        return ResponseEntity.ok().headers(headers).body(response);
    }

    // Utility method to generate a signup token
    private String generateSignUpToken(String username) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[8]; // token length
        secureRandom.nextBytes(randomBytes);
        String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Add hyphens after every 4 characters for readability
        StringBuilder readableToken = new StringBuilder();
        for (int i = 0; i < randomToken.length(); i += 4) {
            readableToken.append(randomToken.substring(i, Math.min(i + 4, randomToken.length())));
            if (i + 4 < randomToken.length()) {
                readableToken.append("-");
            }
        }

        return jwtTokenUtil.generateToken(readableToken.toString()); // return signed token
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
     * Check if a token exists in the tokenMap.
     *
     * @param token The token to check
     * @return ResponseEntity with a JSON response indicating token existence or a 401 Unauthorized response
     */
    @GetMapping("/token/check")
    public ResponseEntity<Object> checkToken(@RequestParam String token) {
        if (tokenMap.containsKey(token)) {
            // return a JSON response indicating success
            return ResponseEntity.ok().body("Token exists");
        } else {
            // return 401 Unauthorized status
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token does not exist or is expired");
        }
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

    /**
     * Perform user login and return an access token.
     *
     * @param user The user object containing the username and password
     * @param bindingResult The binding result for validation
     * @return ResponseEntity with a JSON response containing the access token or a 401 Unauthorized response
     */
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody @Valid User user, BindingResult bindingResult) {
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

        // Check if the user already has a token (assuming the token is stored in tokenMap)
        String existingToken = tokenMap.get(user.getUsername());

        // Check if the existing token is still valid
        if (existingToken != null && jwtTokenUtil.validateToken(existingToken)) {
            // User already has a valid token, return the existing token
            Map<String, Object> response = new HashMap<>();
            response.put("token", existingToken);
            response.put("message", "Login successful using existing token.");
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + existingToken);
            return ResponseEntity.ok().headers(headers).body(response);
        }

        // Generate a token for successful login or invalid credentials
        String newToken = generateToken(user.getUsername(), false); // Generate a token without base64 and hyphen readability

        if (!isValidCredentials) {
            // Invalid credentials, update the token and return it in the response
            tokenMap.put(user.getUsername(), newToken);

            Map<String, Object> response = new HashMap<>();
            response.put("token", newToken);
            response.put("message", "Invalid credentials. New token generated.");
            HttpHeaders headers = new HttpHeaders();
            headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + newToken);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).headers(headers).body(response);
        }

        // Successful login, update the token and return it in the response
        tokenMap.put(user.getUsername(), newToken);

        // Create the response object that includes the token
        Map<String, Object> response = new HashMap<>();
        response.put("token", newToken);
        response.put("message", "Login successful.");

        // Create the response headers and set the token with the Authorization header
        HttpHeaders headers = new HttpHeaders();
        headers.add(AUTHORIZATION_HEADER_NAME, "Bearer " + newToken);

        // Return the response with headers and a success message
        return ResponseEntity.ok().headers(headers).body(response);
    }

    // Utility method to generate a login token with base64 and hyphen readability
    private String generateToken(String username, boolean useBase64WithHyphens) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Add hyphens after every 4 characters for readability if requested
        StringBuilder readableToken = new StringBuilder();
        if (useBase64WithHyphens) {
            for (int i = 0; i < randomToken.length(); i += 4) {
                readableToken.append(randomToken.substring(i, Math.min(i + 4, randomToken.length())));
                if (i + 4 < randomToken.length()) {
                    readableToken.append("-");
                }
            }
            return jwtTokenUtil.generateToken(readableToken.toString()); // return the signed token
        } else {
            // If not using base64 with hyphens, return the original base64 encoded token
            return jwtTokenUtil.generateToken(randomToken);
        }
    }




    @PostMapping("/forgot-password")
    public ResponseEntity<Object> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String newPassword = request.get("password");

        User user = userService.getUserByEmail(email);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        String encodedPassword = passwordEncoder.encode(newPassword);
        String resetToken = generateResetToken(true);

        user.setResetToken(resetToken);
        user.setPassword(encodedPassword);

        userService.saveUser(user);
        userService.sendResetLink(user, resetToken);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Reset link sent to email.");
        response.put("resetToken", resetToken);

        return ResponseEntity.ok(response);
    }


    private String generateResetToken(boolean useBase64WithHyphens) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] randomBytes = new byte[16];
        secureRandom.nextBytes(randomBytes);
        String randomToken = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Add hyphens after every 4 characters for readability
        StringBuilder readableToken = new StringBuilder();
        if (useBase64WithHyphens) {
            for (int i = 0; i < randomToken.length(); i += 4) {
                readableToken.append(randomToken.substring(i, Math.min(i + 4, randomToken.length())));
                if (i + 4 < randomToken.length()) {
                    readableToken.append("-");
                }
            }
            return jwtTokenUtil.generateToken(readableToken.toString()); // Return signed token
        } else {
            return jwtTokenUtil.generateToken(randomToken); // Return original token
        }
    }


}






