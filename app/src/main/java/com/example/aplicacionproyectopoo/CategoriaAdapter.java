package com.example.aplicacionproyectopoo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import modelo.Categoria;

public class CategoriaAdapter extends RecyclerView.Adapter<CategoriaAdapter.ViewHolder> {

    private ArrayList<Categoria> listaCategorias;
    private OnItemClickListener listener;

    public CategoriaAdapter(ArrayList<Categoria> listaCategorias, OnItemClickListener onItemClickListener) {
        this.listaCategorias = listaCategorias;
        this.listener = onItemClickListener; // Asignamos el listener desde el constructor
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_categoria_adapter, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Categoria categoria = listaCategorias.get(position);
        holder.textViewCategoria.setText(categoria.toString());  // O un campo más específico de Categoria como nombre
    }

    @Override
    public int getItemCount() {
        return listaCategorias.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textViewCategoria;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewCategoria = itemView.findViewById(R.id.textViewCategoria);

            // Configuramos el click en el ViewHolder
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(position);  // Pasamos la posición del clic al listener
                    }
                }
            });
        }
    }

    // Interfaz para manejar el clic en los elementos
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
