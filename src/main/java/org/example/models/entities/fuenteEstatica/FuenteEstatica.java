package org.example.models.entities.fuenteEstatica;

import org.example.models.entities.hecho.Hecho;
import org.example.utils.EstadoProcesado;

import java.util.List;

public class FuenteEstatica extends Fuente {
    public String rutaDataset;
    public ILector lector;
    public EstadoProcesado estadoProcesado;
    private final TipoFuente tipoFuente;

    public FuenteEstatica(String rutaDataset, EstadoProcesado estado) {
        this.rutaDataset = rutaDataset;
        this.estadoProcesado = estado;
        this.lector = seleccionarLector(rutaDataset);
        this.tipoFuente = TipoFuente.ESTATICA;
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
    public  List<Hecho> obtenerHechos() {
        List<Hecho> hechos = lector.obtencionHechos(this.rutaDataset);
        this.setEstado(EstadoProcesado.PROCESADO);
        return hechos;
    };

    public void setEstado(EstadoProcesado estado) {
        this.estadoProcesado = estado;
    }

    public EstadoProcesado getEstado() {
        return estadoProcesado;
    }

    public String getRutaDataset(){
        return rutaDataset;
    }

}
