package com.example.helloandroid.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.helloandroid.daos.ProductDao;
import com.example.helloandroid.daos.UserDao;
import com.example.helloandroid.entities.Product;
import com.example.helloandroid.entities.User;

@Database(entities = {User.class, Product.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract ProductDao productDao();
}
