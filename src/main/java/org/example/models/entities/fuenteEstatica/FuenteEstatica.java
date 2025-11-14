package org.example.models.entities.fuenteEstatica;

import lombok.Getter;
import lombok.Setter;
import org.example.models.entities.hecho.Hecho;
import org.example.utils.EstadoProcesado;
import org.example.models.entities.fuenteEstatica.TipoFuente;
import java.util.List;

@Getter
@Setter
public class FuenteEstatica {
    public String nombre;
    public String rutaDataset;
    public ILector lector;
    public EstadoProcesado estadoProcesado;

    public FuenteEstatica(String rutaDataset) {
        this.rutaDataset = rutaDataset;
        this.nombre = this.rutaDataset.substring(0, rutaDataset.length() - 4);
        this.estadoProcesado = EstadoProcesado.NO_PROCESADO;
        this.lector = seleccionarLector(rutaDataset);
    }

    private ILector seleccionarLector(String ruta) {
        if (ruta.endsWith(".csv")) {
            return new LectorCSV();
        } if(ruta.endsWith(".pdf")){
            return new LectorPDF();
        }
        return null;
    }
}
