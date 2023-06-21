package com.example.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public User getUserById(int id) {
        return userRepository.findById(id).orElse(null);
    }

    public Page<User> getAllUsers(String filter, Pageable pageable) {
        Specification<User> specification = (root, query, criteriaBuilder) -> {
            // Add your filtering conditions based on the 'filter' parameter
            // Example: filtering by name
            if (!filter.isEmpty()) {
                return criteriaBuilder.like(root.get("name"), "%" + filter + "%");
            }
            return null; // Return null if no filtering condition is applied
        };

        return userRepository.findAll(specification, pageable);
    }

    public void updateUser(User user) {
        userRepository.save(user);
    }

    public void deleteUser(int id) {
        userRepository.deleteById(id);
    }


}
