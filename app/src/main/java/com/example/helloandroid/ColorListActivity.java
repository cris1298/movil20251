package com.example.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloandroid.adpaters.ColorAdapter;
import com.example.helloandroid.entities.Color;
import com.example.helloandroid.entities.ColorResponse;
import com.example.helloandroid.services.ColorService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ColorListActivity extends AppCompatActivity {

    RecyclerView rvColors;
    boolean isLoading = false;
    boolean isLastPage = false;
    int currentPage = 1;
    SearchView searchColors;

    String busqueda = "";

    List<Color> colorsData = new ArrayList<>();
    ColorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_color_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Toast.makeText(getApplicationContext(), "ColorListActivity onCreate", Toast.LENGTH_SHORT).show();

        FloatingActionButton button = findViewById(R.id.fabGoToColorForm);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormColorActivity.class);
            //startActivity(intent);
            startActivityForResult(intent, 100);
        });

        rvColors = findViewById(R.id.rvListColors);
        rvColors.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerView();
        setUpSearchView();

        loadMoreColors(busqueda);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK) return;

        if (requestCode == 123) {

            String colorJSON = data.getStringExtra("colorJSON");
            Color updatedColor = new Gson().fromJson(colorJSON, Color.class);

            Color color = colorsData.stream().filter(c -> c.id == updatedColor.id)
                    .findFirst().orElse(null);
            int position = colorsData.indexOf(color);

            if (color != null) {
                color.nombre = updatedColor.nombre;
                color.colorHex = updatedColor.colorHex;
                adapter.notifyItemChanged(position); //esto es lo relevante
            }
        }

        if (requestCode == 100) {
            String colorJSON = data.getStringExtra("colorJSON");
            Color createdColor = new Gson().fromJson(colorJSON, Color.class);

            colorsData.add(createdColor);
            adapter.notifyItemInserted(colorsData.size() -1);
        }

    }

    //    @Override
//    protected  void onResume() {
//        super.onResume();
//
//        Toast.makeText(getApplicationContext(), "ColorListActivity onResume", Toast.LENGTH_SHORT).show();
//
//        data.clear();
//        currentPage = 1;
//        adapter.notifyDataSetChanged(); // notifica al adapter que los datos han cambiado
//
//        loadMoreColors();
//    }

    private void loadMoreColors(String query) {

        isLoading = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://67ff051e58f18d7209efd099.mockapi.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ColorService service = retrofit.create(ColorService.class);
        service.getColors(20, currentPage, query).enqueue(new Callback<List<Color>>() {
            @Override
            public void onResponse(Call<List<Color>> call, Response<List<Color>> response) {
                isLoading = false;

                if (!response.isSuccessful()) return;
                if (response.body() == null) return;
                if (response.body().isEmpty()) {
                    isLastPage = true;
                    return;
                }

                colorsData.addAll(response.body()); // añade los nuevos colores a la lista
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Color>> call, Throwable throwable) {
                isLoading = false;
            }
        });
    }

    private void setUpRecyclerView() {

        adapter = new ColorAdapter(colorsData, ColorListActivity.this);
        rvColors.setAdapter(adapter);

        // Scroll Listener nos permite detectar cuando el usuario hace scroll y llega al final de la lista
        // para cargar más datos
        rvColors.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0) {

                        currentPage++;
                        loadMoreColors(busqueda);
                    }
                }
            }
        });
    }

    private void setUpSearchView() {
        searchColors = findViewById(R.id.searchColor);
        searchColors.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d("MAIN_APP", query);

                if(!Objects.equals(busqueda, query)) {
                    colorsData.clear();
                    adapter.notifyDataSetChanged();
                    busqueda = query;
                    currentPage = 1;
                    loadMoreColors(busqueda);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d("MAIN_APP", newText);
                if (newText.isEmpty()) {
                    colorsData.clear();
                    adapter.notifyDataSetChanged();
                    busqueda = "";
                    currentPage = 1;
                    loadMoreColors(busqueda);
                }
                return false;
            }
        });
    }
}