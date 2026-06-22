package com.cathedralapi.cathedralbackenedapi;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Base64;

@Configuration
public class FirebaseConfig {

    @PostConstruct
    public void initializeFirebase() {
        try {
            if (FirebaseApp.getApps().isEmpty()) {
                InputStream serviceAccountStream;

                // 🛰️ 1. Look for the safe, continuous Base64 text string from Railway
                String base64Data = System.getenv("FIREBASE_KEY_BASE64");

                if (base64Data != null && !base64Data.trim().isEmpty()) {
                    System.out.println("🛡️ PRODUCTION: Found Base64 text string. Decoding binary signature...");
                    byte[] pristineBytes = Base64.getDecoder().decode(base64Data.trim());
                    serviceAccountStream = new ByteArrayInputStream(pristineBytes);
                } else {
                    // 🏠 2. Local IntelliJ fallback
                    System.out.println("🏠 LOCAL: Loading local classpath resource file...");
                    serviceAccountStream = new ClassPathResource("firebase-service-account.json").getInputStream();
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(GoogleCredentials.fromStream(serviceAccountStream))
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 SUCCESS: Firebase Admin SDK initialized successfully!");
            }
        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}