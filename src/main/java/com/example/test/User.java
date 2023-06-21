package com.example.test;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Name is required")
    private String name;

    @Email(message = "Invalid email address")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    public User() {
    }

    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters

    /**
     * Returns the ID of the user.
     *
     * @return The user ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets the ID of the user.
     *
     * @param id The user ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Returns the name of the user.
     *
     * @return The username
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the user.
     *
     * @param name The user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the email of the user.
     *
     * @return The user email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email The user email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returns the password of the user.
     *
     * @return The user password
     */
    public String getPassword() {
        return password;
    }
}
