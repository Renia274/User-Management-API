package com.example.test;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Find a user by name.
     *
     * @param name The name of the user to find
     * @return The user object if found, or null otherwise
     */
    User findByName(String name);

    /**
     * Find a user by email.
     *
     * @param email The email of the user to find
     * @return The user object if found, or null otherwise
     */
    User findByEmail(String email);

    /**
     * Find a user by password.
     *
     * @param password The password of the user to find
     * @return The user object if found, or null otherwise
     */
    User findByPassword(String password);

    User findByUsername(String username);

    /**
     * Find all users based on the provided specification, with pagination.
     *
     * @param specification The specification to filter the users
     * @param pageable      The pageable object for pagination and sorting
     * @return The page containing the filtered users
     */
    Page<User> findAll(Specification<User> specification, Pageable pageable);
}
