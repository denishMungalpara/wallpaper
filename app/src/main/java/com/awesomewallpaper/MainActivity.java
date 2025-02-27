package com.awesomewallpaper;

import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    WallpaperAdapter wallpaperAdapter;
    List<WallpaperModel> wallpaperModelList;
    int pageNumber = 1;
    String Url1 = "https://api.pexels.com/v1/curated/?page=" + pageNumber + "&per_page=80";
    private boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;

    Button btn_all,btn_cake,btn_nature, btn_bike, btn_car, btn_ocean, btn_flowers, btn_animal, btn_birds, btn_wild, btn_cool, btn_love, btn_juice, btn_music, btn_watch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize buttons
        btn_bike = findViewById(R.id.btn_bike);
        btn_car = findViewById(R.id.btn_car);
        btn_ocean = findViewById(R.id.btn_ocean);
        btn_flowers = findViewById(R.id.btn_flowers);

        // Set click listeners
        btn_bike.setOnClickListener(this);
        btn_car.setOnClickListener(this);
        btn_ocean.setOnClickListener(this);
        btn_flowers.setOnClickListener(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);
        recyclerView.setAdapter(wallpaperAdapter);

        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = gridLayoutManager.getChildCount();
                totalItems = gridLayoutManager.getItemCount();
                scrollOutItems = gridLayoutManager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fetchWallpaper();
                }
            }
        });

        fetchWallpaper();
    }

    @Override
    public void onClick(View v) {
        if (v == btn_all) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_bike) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=bike";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_car) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=car";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_ocean) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=ocean";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_flowers) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=flowers";
            wallpaperModelList.clear();
            fetchWallpaper();
        }

        // Handle other button clicks...
    }

    public void fetchWallpaper() {
        StringRequest request = new StringRequest(Request.Method.GET, Url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");
                            int length = jsonArray.length();

                            for (int i = 0; i < length; i++) {
                                JSONObject object = jsonArray.getJSONObject(i);
                                int id = object.getInt("id");
                                JSONObject objectImages = object.getJSONObject("src");
                                String originalUrl = objectImages.getString("original");
                                String mediumUrl = objectImages.getString("medium");

                                WallpaperModel wallpaperModel = new WallpaperModel(id, originalUrl, mediumUrl);
                                wallpaperModelList.add(wallpaperModel);
                            }

                            wallpaperAdapter.notifyDataSetChanged();
                            pageNumber++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Authorization", "563492ad6f917000010000011d2e266949824efca8e7780b2b99e886");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }
}