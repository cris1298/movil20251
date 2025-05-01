package com.example.helloandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

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

    List<Color> data = new ArrayList<>();
    ColorAdapter adapter;

    private ActivityResultLauncher<Intent> launcher;

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

        Log.d("MAIN_APP", "onCreate");


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {

                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent iData = result.getData();
                    if (iData != null) {
                        int colorId = iData.getIntExtra("colorId", 0);
                        String colorName = iData.getStringExtra("colorName");
                        Color color = data.stream().filter(c -> c.id == colorId).findFirst().orElse(null);
                        color.nombre = colorName;
                        adapter.notifyDataSetChanged();
                        Log.d("MAIN_APP", "onActivityResult: " + colorId);
                    }
                }
            }
        });


        Toast.makeText(getApplicationContext(), "ColorListActivity onCreate", Toast.LENGTH_SHORT).show();

        FloatingActionButton button = findViewById(R.id.fabGoToColorForm);
        button.setOnClickListener(v -> {
            Intent intent = new Intent(this, FormColorActivity.class);
            launcher.launch(intent);

        });

        rvColors = findViewById(R.id.rvListColors);
        rvColors.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerView();
        loadMoreColors();
    }


    @Override
    protected  void onResume() {
        super.onResume();

//        Toast.makeText(getApplicationContext(), "ColorListActivity onResume", Toast.LENGTH_SHORT).show();
//
//        data.clear();
//        currentPage = 1;
//        adapter.notifyDataSetChanged(); // notifica al adapter que los datos han cambiado
//
//        loadMoreColors();
    }

    private void loadMoreColors() {

        isLoading = true;

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://67ff051e58f18d7209efd099.mockapi.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ColorService service = retrofit.create(ColorService.class);
        service.getColors(20, currentPage).enqueue(new Callback<List<Color>>() {
            @Override
            public void onResponse(Call<List<Color>> call, Response<List<Color>> response) {
                isLoading = false;

                if (!response.isSuccessful()) return;
                if (response.body() == null) return;
                if (response.body().isEmpty()) {
                    isLastPage = true;
                    return;
                }

                data.addAll(response.body()); // añade los nuevos colores a la lista
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Color>> call, Throwable throwable) {
                isLoading = false;
            }
        });
    }


    private void setUpRecyclerView() {

        adapter = new ColorAdapter(data, launcher);
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
                        loadMoreColors();
                    }
                }
            }
        });
    }
}