package com.cathedralapi.cathedralbackenedapi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            InputStream serviceAccountStream;

            // 🔍 1. Read the raw text string directly from Environment Variables
            String rawJsonData = System.getenv("FIREBASE_JSON_DATA");

            if (rawJsonData != null && !rawJsonData.trim().isEmpty()) {
                System.out.println("🛡️ SUCCESS: Found Firebase credentials text inside Environment Variables!");
                serviceAccountStream = new java.io.ByteArrayInputStream(rawJsonData.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            } else {
                // 🔍 2. Fallback for your local IntelliJ environment
                System.out.println("🏠 Environment variable empty. Checking local classpath resources (IntelliJ)...");
                serviceAccountStream = new ClassPathResource("firebase-service-account.json").getInputStream();
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🚀 Firebase Admin SDK has been successfully initialized!");
            }
        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}