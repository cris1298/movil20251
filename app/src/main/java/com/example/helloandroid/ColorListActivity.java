package com.example.helloandroid;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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
            startActivity(intent);
        });

        rvColors = findViewById(R.id.rvListColors);
        rvColors.setLayoutManager(new LinearLayoutManager(this));

        setUpRecyclerView();

        loadMoreColors();

    }


    @Override
    protected  void onResume() {
        super.onResume();

        Toast.makeText(getApplicationContext(), "ColorListActivity onResume", Toast.LENGTH_SHORT).show();

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
                if (response.body().size() == 0) {
                    isLastPage = true;
                    return;
                }


                data.addAll(response.body());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Color>> call, Throwable throwable) {
                isLoading = false;
            }
        });
    }


    private void setUpRecyclerView() {

        adapter = new ColorAdapter(data);
        rvColors.setAdapter(adapter);

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