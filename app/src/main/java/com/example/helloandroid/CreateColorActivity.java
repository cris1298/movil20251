package com.example.helloandroid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.helloandroid.entities.Color;
import com.example.helloandroid.services.ColorService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreateColorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_color);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        String colorName = "blue";
        String colorHex = "#FAA0A0";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://67ff051e58f18d7209efd099.mockapi.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ColorService service = retrofit.create(ColorService.class);

        Color color = new Color();
        color.nombre = colorName; // leer del editText
        color.colorHex = colorHex; // leer del editText

        service.create(color).enqueue(new Callback<Color>() {
            @Override
            public void onResponse(Call<Color> call, Response<Color> response) {
                //mostrar mensaje de se creo color
            }

            @Override
            public void onFailure(Call<Color> call, Throwable throwable) {

            }
        });

    }
}