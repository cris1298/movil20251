package com.example.helloandroid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ColorListActivity extends AppCompatActivity {

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



//        data.add(new Color("Red", "#F44336"));
//        data.add(new Color("Pink", "#E91E63"));
//        data.add(new Color("Blue", "#2196F3"));
//        data.add(new Color("LightSalmon", "#FFA07A"));

        RecyclerView rvColors = findViewById(R.id.rvListColors);
        rvColors.setLayoutManager(new LinearLayoutManager(this));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.csscolorsapi.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ColorService service = retrofit.create(ColorService.class);

        service.getColors().enqueue(new Callback<ColorResponse>() {
            @Override
            public void onResponse(Call<ColorResponse> call, Response<ColorResponse> response) {

                if (!response.isSuccessful()) return;

                List<Color> data = response.body().colors;

                ColorAdapter adapter = new ColorAdapter(data);
                rvColors.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<ColorResponse> call, Throwable throwable) {

            }
        });




    }
}