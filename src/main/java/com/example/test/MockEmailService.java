package com.example.test;


import org.springframework.stereotype.Service;

@Service
public class MockEmailService {

    public void sendResetLink(String email, String resetToken) {
        // Simulate sending an email with a reset link
        System.out.println("Sending email to " + email + " with reset token: " + resetToken);
    }
}

