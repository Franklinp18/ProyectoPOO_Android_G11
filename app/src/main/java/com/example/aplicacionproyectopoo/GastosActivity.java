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

public class GastosActivity extends AppCompatActivity {

    private CategoriaAdapter categoriaAdapter;
    private int selectedIndex = -1;  // Índice del gasto seleccionado

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gastos);  // Asegúrate de que este sea el layout correcto

        // Vinculamos las vistas
        RecyclerView recyclerViewGastos = findViewById(R.id.recyclerViewGastos);
        Button btnAgregarGasto = findViewById(R.id.btnAgregarGasto);
        Button btnEliminarGasto = findViewById(R.id.btnEliminarGasto);

        // Filtramos solo los gastos de la lista general
        ArrayList<Categoria> listaGastos = filtrarGastos();

        // Configuramos el RecyclerView con las categorías filtradas
        categoriaAdapter = new CategoriaAdapter(listaGastos, new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedIndex = position;  // Guardar el índice del elemento seleccionado
                Toast.makeText(GastosActivity.this, "Gasto seleccionado: " + listaGastos.get(position).getNombre(), Toast.LENGTH_SHORT).show();
            }
        });
        recyclerViewGastos.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewGastos.setAdapter(categoriaAdapter);

        // Lógica para agregar una nueva categoría de gasto
        btnAgregarGasto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                mostrarDialogoAgregarGasto(listaGastos);  // Mostrar el cuadro de diálogo para agregar el gasto
            }
        });

        // Lógica para eliminar un gasto seleccionado
        btnEliminarGasto.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                if (selectedIndex != -1) {
                    Categoria categoriaAEliminar = listaGastos.get(selectedIndex);

                    // Eliminar de la lista general y de la lista filtrada
                    AdministrarCategoriasActivity.listaCategorias.remove(categoriaAEliminar);
                    listaGastos.remove(selectedIndex);
                    categoriaAdapter.notifyDataSetChanged();
                    selectedIndex = -1;  // Reiniciar el índice seleccionado

                    // Actualizar el archivo txt
                    guardarCategoriasEnArchivo();
                    Toast.makeText(GastosActivity.this, "Gasto eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GastosActivity.this, "Seleccione un gasto para eliminar", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Método para filtrar solo las categorías de tipo GASTO desde la lista general
    private ArrayList<Categoria> filtrarGastos() {
        ArrayList<Categoria> listaGastos = new ArrayList<>();
        for (Categoria categoria : AdministrarCategoriasActivity.listaCategorias) {
            if (categoria.getTipoCategoria() == TipoCategoria.GASTO) {
                listaGastos.add(categoria);
            }
        }
        return listaGastos;
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

    // Método para mostrar el cuadro de diálogo de agregar gasto
    private void mostrarDialogoAgregarGasto(ArrayList<Categoria> listaGastos) {
        // Usamos un AlertDialog para que el usuario ingrese el nombre del nuevo gasto
        AlertDialog.Builder builder = new AlertDialog.Builder(GastosActivity.this);
        builder.setTitle("Agregar Nuevo Gasto");

        // Inflamos el layout con un EditText para ingresar el nombre del gasto
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_gasto, null, false);
        final EditText inputNombreGasto = viewInflated.findViewById(R.id.inputNombreGasto);
        builder.setView(viewInflated);

        // Configuramos los botones del diálogo
        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreGasto = inputNombreGasto.getText().toString().trim();
                if (!nombreGasto.isEmpty()) {
                    Categoria nuevaCategoria = new Categoria(nombreGasto, TipoCategoria.GASTO);
                    AdministrarCategoriasActivity.listaCategorias.add(nuevaCategoria);  // Agregar a la lista general
                    listaGastos.add(nuevaCategoria);  // Agregar a la lista filtrada
                    categoriaAdapter.notifyDataSetChanged();
                    guardarCategoriasEnArchivo();  // Guardar cambios en el archivo txt
                    Toast.makeText(GastosActivity.this, "Gasto agregado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(GastosActivity.this, "El nombre no puede estar vacío", Toast.LENGTH_SHORT).show();
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
