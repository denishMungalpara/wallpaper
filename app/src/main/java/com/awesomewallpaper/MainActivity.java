package com.awesomewallpaper;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.widget.SearchView;
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
    // UI Elements for loading and no wallpapers message
    ProgressBar progressBar;
    TextView noWallpapersText;

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
        btn_all = findViewById(R.id.btn_all);
        btn_cake = findViewById(R.id.btn_cake);
        btn_nature = findViewById(R.id.btn_nature);
        btn_animal = findViewById(R.id.btn_animal);
        btn_birds = findViewById(R.id.btn_birds);
        btn_wild = findViewById(R.id.btn_wild);
        btn_cool = findViewById(R.id.btn_cool);
        btn_love = findViewById(R.id.btn_love);
        btn_juice = findViewById(R.id.btn_juice);
        btn_music = findViewById(R.id.btn_music);
        btn_watch = findViewById(R.id.btn_watch);

        // Set click listeners
        btn_bike.setOnClickListener(this);
        btn_car.setOnClickListener(this);
        btn_ocean.setOnClickListener(this);
        btn_flowers.setOnClickListener(this);
        btn_all.setOnClickListener(this);
        btn_cake.setOnClickListener(this);
        btn_nature.setOnClickListener(this);
        btn_animal.setOnClickListener(this);
        btn_birds.setOnClickListener(this);
        btn_wild.setOnClickListener(this);
        btn_cool.setOnClickListener(this);
        btn_love.setOnClickListener(this);
        btn_juice.setOnClickListener(this);
        btn_music.setOnClickListener(this);
        btn_watch.setOnClickListener(this);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);
        recyclerView.setAdapter(wallpaperAdapter);

        // Initialize UI elements
        progressBar = findViewById(R.id.progressBar);
        noWallpapersText = findViewById(R.id.no_wallpapers_text);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        wallpaperModelList = new ArrayList<>();
        wallpaperAdapter = new WallpaperAdapter(this, wallpaperModelList);
        recyclerView.setAdapter(wallpaperAdapter);

        // Initialize SearchView
        SearchView searchView = findViewById(R.id.searchView);

        // Set up SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // When user submits the query, clear previous wallpapers and fetch new ones
                Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=" + query;
                wallpaperModelList.clear();  // Clear existing wallpapers
                fetchWallpaper();  // Fetch new wallpapers based on search
                hideKeyboard();  // Hide keyboard after submit
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

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

        // Hide keyboard if user taps outside of SearchView
        View rootView = findViewById(android.R.id.content);
        rootView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == btn_all) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=popular";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_bike) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=motorbike";
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
        if (v == btn_cake) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=cake";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_nature) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=nature";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_animal) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=animal";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_birds) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=birds";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_wild) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=wild";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_cool) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=cool";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_love) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=love";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_juice) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=juice";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_music) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=music";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
        if (v == btn_watch) {
            Url1 = "https://api.pexels.com/v1/search/?page=" + pageNumber + "&per_page=80&query=watch";
            wallpaperModelList.clear();
            fetchWallpaper();
        }
    }

    // Method to hide the keyboard
    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null && getCurrentFocus() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    public void fetchWallpaper() {
        StringRequest request = new StringRequest(Request.Method.GET, Url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("photos");

                            if (jsonArray.length() == 0) {
                                // If no wallpapers found, show the "No wallpapers available" message
                                noWallpapersText.setVisibility(View.VISIBLE);
                            } else {
                                // Hide the "No wallpapers available" message
                                noWallpapersText.setVisibility(View.GONE);

                                // Process the wallpapers and add them to the list
                                for (int i = 0; i < jsonArray.length(); i++) {
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
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            // Hide the loading indicator
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                // Hide the loading indicator in case of error
                progressBar.setVisibility(View.GONE);
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
