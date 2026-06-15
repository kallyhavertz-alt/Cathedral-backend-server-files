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
            InputStream serviceAccountStream;

            // 1. Try to read the JSON content directly from System Environment Variables (For Render Production)
            String firebaseConfigEnv = System.getenv("firebase-service-account.json");

            if (firebaseConfigEnv != null && !firebaseConfigEnv.trim().isEmpty()) {
                System.out.println(" Found Firebase credentials in Environment Variables. Initializing...");
                serviceAccountStream = new ByteArrayInputStream(firebaseConfigEnv.getBytes(StandardCharsets.UTF_8));
            } else {
                // 2. Fallback: Read from the local resources folder (For local machine development)
                System.out.println(" Environment variable not found. Falling back to local class path resource file...");
                serviceAccountStream = new ClassPathResource("firebase-service-account.json").getInputStream();
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("Firebase Admin SDK has been successfully initialized!");
            }
        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}






















/*package com.cathedralapi.cathedralbackenedapi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            // 🎯 Reads your secret credentials file from the src/main/resources folder
            InputStream serviceAccount = new ClassPathResource("firebase-service-account.json").getInputStream();

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build();

            if (FirebaseApp.getApps().isEmpty()) {
                FirebaseApp.initializeApp(options);
                System.out.println("🚀 Firebase Admin SDK has been successfully initialized!");
            }
        } catch (Exception e) {
            System.err.println("❌ Failed to initialize Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}*/
