package com.example.aplicacionproyectopoo;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import modelo.Categoria;
import modelo.enums.TipoCategoria;

public class IngresosActivity extends AppCompatActivity {

    private CategoriaAdapter categoriaAdapter;
    private int selectedIndex = -1;  // Índice del ingreso seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingresos);  // Asegúrate de que este sea el layout correcto

        // Vinculamos las vistas
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) RecyclerView recyclerViewIngresos = findViewById(R.id.recyclerViewIngresos);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnAgregarIngreso = findViewById(R.id.btnAgregarIngreso);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnEliminarIngreso = findViewById(R.id.btnEliminarIngreso);

        // Filtramos solo los ingresos de la lista general
        ArrayList<Categoria> listaIngresos = filtrarIngresos();

        // Configuramos el RecyclerView con las categorías filtradas
        categoriaAdapter = new CategoriaAdapter(listaIngresos, new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedIndex = position;  // Guardar el índice del elemento seleccionado
                Toast.makeText(IngresosActivity.this, "Ingreso seleccionado: " + listaIngresos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewIngresos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngresos.setAdapter(categoriaAdapter);

        // Lógica para agregar una nueva categoría de ingreso
        btnAgregarIngreso.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarIngreso(listaIngresos);  // Mostrar el cuadro de diálogo para agregar el ingreso
            }
        });

        // Lógica para eliminar un ingreso seleccionado
        btnEliminarIngreso.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (selectedIndex != -1) {
                    Categoria categoriaAEliminar = listaIngresos.get(selectedIndex);

                    // Eliminar de la lista general y de la lista filtrada
                    AdministrarCategoriasActivity.listaCategorias.remove(categoriaAEliminar);
                    listaIngresos.remove(selectedIndex);
                    categoriaAdapter.notifyDataSetChanged();
                    selectedIndex = -1;  // Reiniciar el índice seleccionado

                    // Actualizar el archivo txt
                    guardarCategoriasEnArchivo();
                    Toast.makeText(IngresosActivity.this, "Ingreso eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IngresosActivity.this, "Seleccione un ingreso para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para filtrar solo las categorías de tipo INGRESO desde la lista general
    private ArrayList<Categoria> filtrarIngresos() {
        ArrayList<Categoria> listaIngresos = new ArrayList<>();
        for (Categoria categoria : AdministrarCategoriasActivity.listaCategorias) {
            if (categoria.getTipoCategoria() == TipoCategoria.INGRESO) {
                listaIngresos.add(categoria);
            }
        }
        return listaIngresos;
    }

    // Método para guardar las categorías en el archivo txt
    private void guardarCategoriasEnArchivo() {
        try {
            FileOutputStream fos = openFileOutput("categorias.txt", MODE_PRIVATE);
            for (Categoria categoria : AdministrarCategoriasActivity.listaCategorias) {
                String linea = categoria.getNombre() + "," + categoria.getTipoCategoria().name() + "\n";
                fos.write(linea.getBytes());
            }
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar el cuadro de diálogo de agregar ingreso
    private void mostrarDialogoAgregarIngreso(ArrayList<Categoria> listaIngresos) {
        // Usamos un AlertDialog para que el usuario ingrese el nombre del nuevo ingreso
        AlertDialog.Builder builder = new AlertDialog.Builder(IngresosActivity.this);
        builder.setTitle("Agregar Nuevo Ingreso");

        // Inflamos el layout con un EditText para ingresar el nombre del ingreso
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_ingreso, null, false);
        final EditText inputNombreIngreso = viewInflated.findViewById(R.id.inputNombreIngreso);
        builder.setView(viewInflated);

        // Configuramos los botones del diálogo
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreIngreso = inputNombreIngreso.getText().toString().trim();
                if (!nombreIngreso.isEmpty()) {
                    Categoria nuevaCategoria = new Categoria(nombreIngreso, TipoCategoria.INGRESO);
                    AdministrarCategoriasActivity.listaCategorias.add(nuevaCategoria);  // Agregar a la lista general
                    listaIngresos.add(nuevaCategoria);  // Agregar a la lista filtrada
                    categoriaAdapter.notifyDataSetChanged();
                    guardarCategoriasEnArchivo();  // Guardar cambios en el archivo txt
                    Toast.makeText(IngresosActivity.this, "Ingreso agregado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(IngresosActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        // Mostramos el cuadro de diálogo
        builder.show();
    }
}
