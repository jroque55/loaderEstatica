package org.example.models.dtos;

import lombok.Data;
import org.example.models.entities.fuenteEstatica.TipoFuente;

@Data
public class FuenteDto {
    private String nombre;
    private final String ruta;
    private final TipoFuente tipoFuente;

    public void obtenerNombre() {
        nombre = this.ruta.substring(0, ruta.length() - 4);
    }
}
