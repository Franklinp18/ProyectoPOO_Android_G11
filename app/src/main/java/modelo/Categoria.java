package modelo;
import androidx.annotation.NonNull;

import modelo.enums.TipoCategoria;

public class Categoria {
        private String nombre;
        private TipoCategoria tipoCategoria;

        public Categoria(String nombre, TipoCategoria tipoCategoria) {
            this.nombre = nombre;
            this.tipoCategoria = tipoCategoria;
        }

        public String getNombre() {
            return nombre;
        }

        public TipoCategoria getTipoCategoria() {
            return tipoCategoria;
        }

        @NonNull
        @Override
        public String toString() {
            return nombre + " (" + tipoCategoria + ")";
        }
    }


