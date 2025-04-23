package com.awesomewallpaper;

import android.app.WallpaperManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FullScreenActivity extends AppCompatActivity {

    private ImageView fullScreenImageView;
    private Button btnSetWallpaper, btnDownloadImage;
    private String imageUrl;
    private static final int STORAGE_PERMISSION_CODE = 100;
    private ImageView goBackButton;
    private ImageView shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        // Initialize views
        fullScreenImageView = findViewById(R.id.fullscreen_image);
        btnSetWallpaper = findViewById(R.id.btn_set_wallpaper);
        btnDownloadImage = findViewById(R.id.btn_download_image);
        goBackButton = findViewById(R.id.go_back_button);
        shareButton = findViewById(R.id.share_button);

        // Get the image URL passed from MainActivity
        imageUrl = getIntent().getStringExtra("image_url");

        // Load the image into ImageView using Glide
        Glide.with(this)
                .load(imageUrl)
                .into(fullScreenImageView);

        // Set click listeners for buttons
        btnSetWallpaper.setOnClickListener(v -> SetWallpaperEvent());
        btnDownloadImage.setOnClickListener(v -> downloadImage());
        // Go Back Button Functionality
        goBackButton.setOnClickListener(v -> onBackPressed());
        // Share Button Functionality
        shareButton.setOnClickListener(v -> shareImage());
    }
    public void SetWallpaperEvents() {

        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Bitmap bitmap  = ((BitmapDrawable)fullScreenImageView.getDrawable()).getBitmap();
        try {
            wallpaperManager.setBitmap(bitmap);
            Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SetWallpaperEvent() {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);
        Bitmap originalBitmap = ((BitmapDrawable) fullScreenImageView.getDrawable()).getBitmap();

        int desiredWidth = wallpaperManager.getDesiredMinimumWidth();
        int desiredHeight = wallpaperManager.getDesiredMinimumHeight();

        // Create centered wallpaper based on orientation
        Bitmap finalWallpaper = createCenteredBitmap(originalBitmap, desiredWidth, desiredHeight);

        try {
            wallpaperManager.setBitmap(finalWallpaper);
            Toast.makeText(this, "Wallpaper Set", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to set wallpaper.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap createCenteredBitmap(Bitmap source, int desiredWidth, int desiredHeight) {
        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();

        // Check if image is portrait or landscape
        boolean isPortrait = srcHeight > srcWidth;

        float scale;
        if (isPortrait) {
            // Scale based on height
            scale = (float) desiredHeight / srcHeight;
        } else {
            // Scale based on width
            scale = (float) desiredWidth / srcWidth;
        }

        int scaledWidth = Math.round(srcWidth * scale);
        int scaledHeight = Math.round(srcHeight * scale);

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(source, scaledWidth, scaledHeight, true);

        // Center crop
        int xOffset = Math.max((scaledWidth - desiredWidth) / 2, 0);
        int yOffset = Math.max((scaledHeight - desiredHeight) / 2, 0);

        return Bitmap.createBitmap(scaledBitmap, xOffset, yOffset,
                Math.min(desiredWidth, scaledWidth),
                Math.min(desiredHeight, scaledHeight));
    }

    // Method to set the image as wallpaper
    private void setWallpaper() {
        try {
            // Fetch the image as Bitmap from the URL
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();

            // Get the input stream of the image
            InputStream inputStream = connection.getInputStream();

            // Create a Bitmap from the input stream
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

            // Get the WallpaperManager instance
            WallpaperManager wallpaperManager = WallpaperManager.getInstance(this);

            // Set the image as wallpaper
            wallpaperManager.setBitmap(bitmap);

            // Show a Toast message
            Toast.makeText(FullScreenActivity.this, "Wallpaper set successfully!", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(FullScreenActivity.this, "Failed to set wallpaper.", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to share the image URL
    private void shareImage() {
        // Create an Intent to share the image URL
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, imageUrl);  // Share the image URL

        // Start the sharing intent
        startActivity(Intent.createChooser(shareIntent, "Share Image URL"));
    }

    // Method to share the image
    private void shareImages() {
        // Get the bitmap from the ImageView
        Bitmap bitmap = ((BitmapDrawable) fullScreenImageView.getDrawable()).getBitmap();

        // Save the bitmap to external storage
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Downloaded_Wallpapers");
        if (!storageDir.exists()) {
            storageDir.mkdirs();
        }

        // Save the file with a unique name
        File file = new File(storageDir, "shared_wallpaper_" + System.currentTimeMillis() + ".jpg");

        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            // Use MediaScannerConnection to scan the file and make it appear in the gallery
            MediaScannerConnection.scanFile(this,
                    new String[]{file.getAbsolutePath()},
                    null,
                    (path, uri) -> {
                        // Intent to share the image
                        Uri uriForShare = Uri.fromFile(file);
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("image/jpeg");
                        shareIntent.putExtra(Intent.EXTRA_STREAM, uriForShare);
                        startActivity(Intent.createChooser(shareIntent, "Share Image"));
                    });
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to share image.", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyImageUrlToClipboard() {
        // Copy URL to clipboard
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Image URL", imageUrl);
        clipboard.setPrimaryClip(clip);

        Toast.makeText(this, "Image URL copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    // Check and request storage permissions
//    private void checkStoragePermissionAndDownloadImage() {
//        // Check if permission is granted
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//            // Permission granted, proceed with download
//            downloadImage();
//        } else {
//            // Request permission
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                    STORAGE_PERMISSION_CODE);
//        }
//    }

    // Handle the permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with download
                downloadImage();
            } else {
                // Permission denied, show a toast
                Toast.makeText(this, "Permission denied. Cannot download image.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to download the image
    private void downloadImage() {
        new Thread(() -> {
            try {
                // Create a URL object from the image URL
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();

                // Get the input stream of the image
                InputStream inputStream = connection.getInputStream();

                // Create a Bitmap from the input stream
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);

                // Save the bitmap to external storage
                File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Downloaded_Wallpapers");
                if (!storageDir.exists()) {
                    storageDir.mkdirs();
                }
                File file = new File(storageDir, "wallpaper_" + System.currentTimeMillis() + ".jpg");

                FileOutputStream fileOutputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();

                // Use MediaScannerConnection to scan the file and make it appear in the gallery
                MediaScannerConnection.scanFile(FullScreenActivity.this,
                        new String[]{file.getAbsolutePath()},
                        null,
                        (path, uri) -> Toast.makeText(FullScreenActivity.this, "Image downloaded successfully!", Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(FullScreenActivity.this, "Failed to download image.", Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

}
