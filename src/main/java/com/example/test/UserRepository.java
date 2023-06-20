package com.example.test;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    // Find users by name containing a given value (case-insensitive) with pagination
    Page<User> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Find users by email containing a given value (case-insensitive) with pagination
    Page<User> findByEmailContainingIgnoreCase(String email, Pageable pageable);
}
