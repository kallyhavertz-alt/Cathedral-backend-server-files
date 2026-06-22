package com.cathedralapi.cathedralbackenedapi;

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
            if (FirebaseApp.getApps().isEmpty()) {
                GoogleCredentials credentials;

                // 🛰️ 1. Check if Google's native environment variable is present (Railway)
                if (System.getenv("GOOGLE_APPLICATION_CREDENTIALS") != null) {
                    System.out.println("🛡️ SUCCESS: GOOGLE_APPLICATION_CREDENTIALS detected. Using native IAM injection layer...");
                    credentials = GoogleCredentials.getApplicationDefault();
                } else {
                    // 🏠 2. Local Fallback (IntelliJ Execution Runner)
                    System.out.println("静态 Context: Environment variable absent. Loading local classpath resource file...");
                    InputStream localStream = new ClassPathResource("firebase-service-account.json").getInputStream();
                    credentials = GoogleCredentials.fromStream(localStream);
                }

                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();

                FirebaseApp.initializeApp(options);
                System.out.println("🚀 Firebase Admin SDK has been successfully initialized natively!");
            }
        } catch (Exception e) {
            System.err.println("❌ Critical Failure initializing Firebase Admin SDK: " + e.getMessage());
            e.printStackTrace();
        }
    }
}