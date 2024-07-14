package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Save a user.
     *
     * @param user The user object to be saved
     */
    public void saveUser(User user) {
        // Encrypt password before saving
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        userRepository.save(user);
    }


    /**
     * Get a user by ID.
     *
     * @param id The ID of the user to retrieve
     * @return The user object if found, or null otherwise
     */
    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    /**
     * Get all users with filtering and pagination options.
     *
     * @param filter   The filter to apply to the username (default: "")
     * @param pageable The pageable object for pagination and sorting
     * @return The page containing the filtered users
     */
    public Page<User> getAllUsers(String filter, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            // Add your filtering conditions based on the 'filter' parameter
            if (!filter.isEmpty()) {
                return criteriaBuilder.like(root.get("name"), "%" + filter + "%");
            }
            return null; // Return null if no filtering condition is applied
        };

        return userRepository.findAll(specification, pageable);
    }

    /**
     * Update an existing user.
     *
     * @param user The updated user object
     */
    public void updateUser(User user) {
        userRepository.save(user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id The ID of the user to delete
     */
    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }

    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }


    public boolean customLogin(String username, String password) {
        // Retrieve user from database
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return false; // User not found
        }

        // Compare provided password with stored hashed password
        return passwordEncoder.matches(password, user.getPassword());
    }


}
