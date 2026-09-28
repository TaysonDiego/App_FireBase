package com.example.projeto;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import Modelo.FilmeActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int tempo = 4500;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable(){
            public void run(){
                Intent intent = new Intent(MainActivity.this, FilmeActivity.class);
                startActivity(intent);

                finish();
            }
        },tempo);
    }
}