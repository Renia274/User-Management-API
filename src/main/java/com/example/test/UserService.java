package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        // Save the user to the database
        userRepository.save(user);
    }

    public User registerUser(User user) {
        // Register a new user by saving them to the database
        return userRepository.save(user);
    }

    public User getUserById(int id) {
        // Retrieve a user from the database by their ID
        return userRepository.findById(id).orElse(null);
    }

    public List<User> getAllUsers() {
        // Retrieve all users from the database
        return userRepository.findAll();
    }

    public void updateUser(User user) {
        // Update an existing user by saving the changes to the database
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        // Delete a user from the database by their ID
        userRepository.deleteById(id);
    }
 }
