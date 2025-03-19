package com.awesomewallpaper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash); // The layout for the splash screen

        ImageView splashGif = findViewById(R.id.splashGif);  // The ImageView to display the GIF
        Glide.with(this).asGif().load(R.drawable.splash_animation).into(splashGif);

        // Check if the user ID is stored in SharedPreferences (indicating the user is already logged in)
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = sharedPreferences.getString("userId", null);

        // Log.d("SplashActivity", "User ID: " + userId); // This logs the userId in Logcat

        // Check if user is already logged in
        new Handler().postDelayed(() -> {
            Intent intent;
            if (userId != null) {
                // If user is already logged in, skip the login screen and go to MainActivity
                intent = new Intent(SplashActivity.this, MainActivity.class);
            } else {
                // If no user ID, show the login screen
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            // Start the activity with a custom transition animation
            startActivity(intent);

            // Set the transition animation (fade-in and fade-out)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Finish the SplashActivity so the user can't go back to it
            finish();
        }, 3000); // Show splash screen for 3 seconds
    }
}
