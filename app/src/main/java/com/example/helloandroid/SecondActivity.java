package com.example.helloandroid;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.helloandroid.adpaters.BasicAdapter;

import java.util.ArrayList;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        List<String> data = new ArrayList<>();
        data.add("Dato 1");
        data.add("Dato 2");
        data.add("Dato 3");
        data.add("Dato 4");
        data.add("Dato 5");
        data.add("Dato 6");
        data.add("Dato 7");
        data.add("Dato 8");
        data.add("Dato 9");
        data.add("Dato 10");

        RecyclerView rvBasic = findViewById(R.id.rvBasic);
        rvBasic.setLayoutManager(new LinearLayoutManager(this));
        BasicAdapter adapter = new BasicAdapter(data);
        rvBasic.setAdapter(adapter);
    }
}