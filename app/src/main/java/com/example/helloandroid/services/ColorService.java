package com.example.helloandroid.services;

import com.example.helloandroid.entities.Color;
import com.example.helloandroid.entities.ColorResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ColorService {
    // https://run.mocky.io/v3/b3da6d5b-5c2a-4812-8390-6a2d86ee7899?
    @GET("/api/colors")
    Call<ColorResponse> getColors();

    @GET("/api/animes")
    Call<ColorResponse> getAnimes();
}
