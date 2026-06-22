package com.cathedralapi.cathedralbackenedapi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            InputStream serviceAccountStream;

            // 🔍 1. First, check for the robust Base64 environment variable (Railway Production)
            String base64JsonData = System.getenv("FIREBASE_CONFIG_BASE64");

            if (base64JsonData != null && !base64JsonData.trim().isEmpty()) {
                System.out.println("🛡️ SUCCESS: Found Base64 Firebase credentials inside Environment Variables!");

                // 🔓 Cleanly decode the indestructible string into normal JSON bytes
                byte[] decodedBytes = Base64.getDecoder().decode(base64JsonData.trim());
                serviceAccountStream = new ByteArrayInputStream(decodedBytes);

            } else {
                // 🔍 2. Fallback check for the old text key just in case
                String rawJsonData = System.getenv("FIREBASE_JSON_DATA");

                if (rawJsonData != null && !rawJsonData.trim().isEmpty()) {
                    System.out.println("⚠️ WARNING: Falling back to raw text variable string...");
                    serviceAccountStream = new ByteArrayInputStream(rawJsonData.getBytes(StandardCharsets.UTF_8));
                } else {
                    // 🏠 3. Local Development Fallback (IntelliJ local execution runner)
                    System.out.println("🏠 Environment variables empty. Checking local classpath resources (IntelliJ)...");
                    serviceAccountStream = new ClassPathResource("firebase-service-account.json").getInputStream();
                }
            }

            // Build options and register application instance safely
            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 Firebase Admin SDK has been successfully initialized!");
            } else {
                System.out.println("ℹ️ Firebase App instance already initialized. Skipping configuration phase.");
            }

        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}