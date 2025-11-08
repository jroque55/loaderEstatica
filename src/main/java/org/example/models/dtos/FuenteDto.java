package org.example.models.dtos;

import lombok.Data;
import org.example.models.entities.fuenteEstatica.TipoFuente;

@Data
public class FuenteDto {
    private final String ruta;
    private final TipoFuente tipoFuente;
}
