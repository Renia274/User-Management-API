package com.example.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class MockEmailService {

    @Value("${reset.link.expiry.minutes}")
    private int expiryMinutes;

    private final ConcurrentHashMap<String, Instant> tokenStore = new ConcurrentHashMap<>();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    public void sendResetLink(String email, String resetToken) {
        // Generate the reset link
        String resetLink = "http://localhost:8081/forgot-password?token=" + resetToken;

        // Calculate the expiry time and format it
        Instant expiryTime = Instant.now().plusMillis((long) expiryMinutes * 60 * 1000);
        String formattedExpiryTime = FORMATTER.format(expiryTime);

        // Save the link and token to a file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("reset-links.txt", true))) {
            writer.write("Email: " + email + "\n");
            writer.write("Reset Link: " + resetLink + "\n");
            writer.write("Token: " + resetToken + "\n");
            writer.write("Expiry: " + formattedExpiryTime + "\n");
            writer.write("------------------------------\n");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Store the token and its expiry time in memory
        tokenStore.put(resetToken, expiryTime);
    }

}
