package org.example.models.entities.fuenteEstatica;

import lombok.Getter;
import lombok.Setter;
import org.example.models.entities.hecho.Hecho;
import org.example.utils.EstadoProcesado;
import java.util.List;

@Getter
@Setter
public class FuenteEstatica extends Fuente {
    public String rutaDataset;
    public ILector lector;
    public EstadoProcesado estadoProcesado;

    public FuenteEstatica(String rutaDataset, EstadoProcesado estado) {
        super(rutaDataset, TipoFuente.ESTATICA);
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

    @Override
    public  List<Hecho> obtenerHechos(String rutaBase) {
        List<Hecho> hechos = lector.obtencionHechos(rutaBase + '/' +this.rutaDataset);
        this.setEstadoProcesado(EstadoProcesado.PROCESADO);
        return hechos;
    };


}
