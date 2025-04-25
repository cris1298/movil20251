package com.example.helloandroid.entities;

import com.google.gson.annotations.SerializedName;

public class Color {


    public int id;

    @SerializedName("name")
    public String nombre;

    @SerializedName("color_hex")
    public String colorHex;

    public Color() {

    }

    public Color(String nombre, String colorHex) {
        this.nombre = nombre;
        this.colorHex = colorHex;
    }
}
