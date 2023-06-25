package com.example.test;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty
    private String name;

    @NotEmpty
//    @Pattern(regexp = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@"
//            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\[A-Za-z]{2,})$")
    private String email;

    @NotEmpty
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,50}$")
    private String username;

    @NotEmpty
//    @Pattern(regexp = "^(?=.*[a-z])(?=.*\\d)(?=.*[A-Z]).{8,50}$")
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


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
