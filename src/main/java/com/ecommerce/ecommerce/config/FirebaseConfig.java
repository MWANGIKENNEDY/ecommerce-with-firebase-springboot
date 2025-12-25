package com.ecommerce.ecommerce.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Value("${firebase.service.account.path:src/main/resources/serviceAccountKey.json}")
    private String serviceAccountPath;

    @Value("${firebase.service.account.json:#{null}}")
    private String serviceAccountJson;

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        GoogleCredentials credentials;

        // Priority 1: Use JSON string from environment variable (for production/cloud)
        if (serviceAccountJson != null && !serviceAccountJson.isEmpty()) {
            InputStream serviceAccountStream = new java.io.ByteArrayInputStream(
                    serviceAccountJson.getBytes(java.nio.charset.StandardCharsets.UTF_8)
            );
            credentials = GoogleCredentials.fromStream(serviceAccountStream);
        } else {
            // Priority 2: Use file path (for local development)
            FileInputStream serviceAccount = new FileInputStream(serviceAccountPath);
            credentials = GoogleCredentials.fromStream(serviceAccount);
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(credentials)
                .build();

        // Check if Firebase is already initialized
        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        } else {
            return FirebaseApp.getInstance();
        }
    }
}
