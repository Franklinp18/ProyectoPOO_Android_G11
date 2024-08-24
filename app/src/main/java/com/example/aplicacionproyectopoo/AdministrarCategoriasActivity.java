package com.example.aplicacionproyectopoo;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import modelo.Categoria;
import modelo.enums.TipoCategoria;

public class AdministrarCategoriasActivity extends AppCompatActivity {

    public static ArrayList<Categoria> listaCategorias; // Lista general de categorías (tanto gastos como ingresos)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_categorias);

        Button btnGastos = findViewById(R.id.btnGastos);
        Button btnIngresos = findViewById(R.id.btnIngresos);

        // Leer el archivo txt y cargar los datos en la lista general
        listaCategorias = leerCategoriasDesdeArchivo();

        // Navegar a la pantalla de Gastos
        btnGastos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministrarCategoriasActivity.this, GastosActivity.class);
                startActivity(intent);
            }
        });

        // Navegar a la pantalla de Ingresos
        btnIngresos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdministrarCategoriasActivity.this, IngresosActivity.class);
                startActivity(intent);
            }
        });
    }

    // Método para leer el archivo txt
    private ArrayList<Categoria> leerCategoriasDesdeArchivo() {
        ArrayList<Categoria> categorias = new ArrayList<>(); // Inicializamos la lista

        try {
            FileInputStream fis = openFileInput("categorias.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));

            String linea;
            while ((linea = reader.readLine()) != null) {
                // Verificamos si la línea está bien formada
                String[] partes = linea.split(",");
                if (partes.length == 2) {
                    String nombre = partes[0];
                    try {
                        TipoCategoria tipo = TipoCategoria.valueOf(partes[1].toUpperCase());
                        categorias.add(new Categoria(nombre, tipo));
                    } catch (IllegalArgumentException e) {
                        // Manejo de error si el tipo de categoría no es válido
                        e.printStackTrace();
                    }
                } else {
                    // Línea malformada, saltar o loggear el error
                    System.err.println("Línea malformada: " + linea);
                }
            }
            reader.close();
        } catch (FileNotFoundException e) {
            // Manejo si el archivo no existe (primera ejecución)
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return categorias;
    }
}
