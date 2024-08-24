package com.example.aplicacionproyectopoo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Vincular el botón del layout con la clase Java
        Button btnAdministrarCategorias = findViewById(R.id.btnAdministrarCategorias);

        // Configurar el comportamiento del botón
        btnAdministrarCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdministrarCategoriasActivity.class);
            startActivity(intent);
        });
    }
}
