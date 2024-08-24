package com.example.aplicacionproyectopoo;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Verificar y copiar el archivo desde res/raw si no existe en almacenamiento interno
        copiarArchivoDesdeRawSiNoExiste();

        // Vincular el botón del layout con la clase Java
        Button btnAdministrarCategorias = findViewById(R.id.btnAdministrarCategorias);

        // Configurar el comportamiento del botón
        btnAdministrarCategorias.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AdministrarCategoriasActivity.class);
            startActivity(intent);
        });
    }

    // Método para copiar el archivo desde res/raw al almacenamiento interno si no existe
    private void copiarArchivoDesdeRawSiNoExiste() {
        File archivo = new File(getFilesDir(), "categorias.txt");

        // Solo copiar el archivo si no existe en almacenamiento interno
        if (!archivo.exists()) {
            try {
                // Abrir el archivo desde res/raw
                InputStream is = getResources().openRawResource(R.raw.categorias); // Asegúrate de tener categorias.txt en res/raw
                FileOutputStream fos = new FileOutputStream(archivo);

                // Buffer para leer el archivo y escribirlo en almacenamiento interno
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, length);
                }

                // Cerrar los streams
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
