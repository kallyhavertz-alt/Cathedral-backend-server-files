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

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccountStream;

                // 🛰️ 1. Look for the custom raw content environment variable (Railway)
                String rawJsonData = System.getenv("FIREBASE_AUTH_STRING");

                if (rawJsonData != null && !rawJsonData.trim().isEmpty()) {
                    System.out.println("🛡️ SUCCESS: Found raw Firebase JSON text data inside Environment Variables!");
                    // Convert raw text strings straight into memory byte arrays safely
                    serviceAccountStream = new ByteArrayInputStream(rawJsonData.getBytes(StandardCharsets.UTF_8));
                } else {
                    // 🏠 2. Local Fallback Configuration (IntelliJ local execution runner)
                    System.out.println("🏠 Environment variable empty. Checking local classpath resources (IntelliJ)...");
                    serviceAccountStream = new ClassPathResource("firebase-service-account.json").getInputStream();
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 Firebase Admin SDK has been successfully initialized via safe text stream!");
            } else {
                System.out.println("ℹ️ Firebase App already initialized. Skipping initialization phase.");
            }
        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}