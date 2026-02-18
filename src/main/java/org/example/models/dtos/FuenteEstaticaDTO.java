package org.example.models.dtos;

import lombok.Data;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.TipoFuente;
import org.example.utils.EstadoProcesado;

@Data
public class FuenteEstaticaDTO {
    private String nombre;
    private final String ruta;
    private final EstadoProcesado estadoProcesado;

    public FuenteEstaticaDTO (FuenteEstatica fe){
        this.nombre = fe.getNombre();
        this.ruta = fe.getRutaDataset();
        this.estadoProcesado = fe.getEstadoProcesado();
    }

}
