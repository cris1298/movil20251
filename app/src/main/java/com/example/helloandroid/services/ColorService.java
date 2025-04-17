package com.example.helloandroid.services;

import com.example.helloandroid.entities.Color;
import com.example.helloandroid.entities.ColorResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ColorService {

//    https://67ff051e58f18d7209efd099.mockapi.io/colores/limit=10&page=3

    @GET("/colores")
    Call< List<Color> > getColors(@Query("limit") int limit, @Query("page") int page);

    @POST("/colores")
    Call<Color> create(@Body Color color);

    @PUT("/colores/{id}")
    Call<Color> update(@Path("id") int id, @Body Color color);

    @DELETE("/colores/{id}")
    Call<Color> delete(@Path("id") int id);
}
