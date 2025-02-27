package com.awesomewallpaper;

import android.content.Intent;
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

        // After 3 seconds, transition to the MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Close SplashActivity so the user can't return to it
            }
        }, 3000); // 3 seconds delay
}
}
