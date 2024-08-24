package com.example.aplicacionproyectopoo;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import modelo.*;
import modelo.enums.TipoCategoria;

import com.example.aplicacionproyectopoo.CategoriaAdapter;

import java.util.ArrayList;

public class AdministrarCategoriasActivity extends AppCompatActivity {

    private RecyclerView recyclerViewCategorias;
    private CategoriaAdapter categoriaAdapter;
    private ArrayList<Categoria> listaCategorias;
    private Button btnAgregarCategoria, btnEliminarCategoria;
    private int selectedPosition = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrar_categorias);

        // Inicializar vistas
        recyclerViewCategorias = findViewById(R.id.recyclerViewCategorias);
        btnAgregarCategoria = findViewById(R.id.btnAgregarCategoria);
        btnEliminarCategoria = findViewById(R.id.btnEliminarCategoria);

        // Inicializar la lista de categorías
        listaCategorias = new ArrayList<>();
        listaCategorias.add(new Categoria("Salario", TipoCategoria.INGRESO));
        listaCategorias.add(new Categoria("Alquiler", TipoCategoria.GASTO));
        listaCategorias.add(new Categoria("Comida", TipoCategoria.GASTO));

        // Configurar el RecyclerView y el adaptador
        categoriaAdapter = new CategoriaAdapter(listaCategorias);
        recyclerViewCategorias.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewCategorias.setAdapter(categoriaAdapter);

        // Configurar el clic en los ítems del RecyclerView
        categoriaAdapter.setOnItemClickListener(new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                selectedPosition = position;
            }
        });

        // Botón para agregar una nueva categoría
        btnAgregarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddCategoryDialog();
            }
        });

        // Botón para eliminar la categoría seleccionada
        btnEliminarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition != -1) {
                    listaCategorias.remove(selectedPosition);
                    categoriaAdapter.notifyItemRemoved(selectedPosition);
                    selectedPosition = -1;
                }
            }
        });
    }

    // Mostrar diálogo para agregar una nueva categoría
    private void showAddCategoryDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Agregar Categoría");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Agregar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String nombreCategoria = input.getText().toString();
                // Puedes decidir si es INCOME o EXPENSE aquí
                Categoria nuevaCategoria = new Categoria(nombreCategoria, TipoCategoria.GASTO);
                listaCategorias.add(nuevaCategoria);
                categoriaAdapter.notifyItemInserted(listaCategorias.size() - 1);
            }
        });

        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}
