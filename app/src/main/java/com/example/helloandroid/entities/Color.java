package com.example.helloandroid.entities;

import com.google.gson.annotations.SerializedName;

public class Color {


    @SerializedName("name")
    public String nombre;

    @SerializedName("hex")
    public String colorHex;


    public Color(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex =colorHex;
    }
}
