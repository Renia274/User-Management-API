package com.example.test;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    // Find a user by their name
    User findByName(String name);
    
    // Find a user by their email
    User findByEmail(String email);
    
    // Find a user by their password
    User findByPassword(String password);
}

