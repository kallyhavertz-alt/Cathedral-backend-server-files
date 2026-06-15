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
            InputStream serviceAccountStream = null;

            // 🔍 1. Look in Render's standard root mounting directory
            File renderSecretRoot = new File("/opt/render/project/src/firebase-service-account.json");
            // 🔍 2. Look in the local execution directory
            File localSecretRoot = new File("firebase-service-account.json");

            if (renderSecretRoot.exists()) {
                System.out.println("🛡️ SUCCESS: Found Firebase credentials at absolute Render Path!");
                serviceAccountStream = new FileInputStream(renderSecretRoot);
            } else if (localSecretRoot.exists()) {
                System.out.println("🛡️ SUCCESS: Found Firebase credentials at relative Local Path!");
                serviceAccountStream = new FileInputStream(localSecretRoot);
            } else {
                // 🔍 3. Fallback to inside the JAR resources folder (local machine IDE test)
                System.out.println("🏠 No external file detected. Checking inside src/main/resources folder...");
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