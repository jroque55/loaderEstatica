package org.example.models.entities.fuenteEstatica;

import org.example.models.entities.hecho.Hecho;

import java.util.List;

public class FuenteEstatica extends Fuente {
    public String rutaDataset;
    public ILector lector;
    private Boolean procesada;
    private final TipoFuente tipoFuente;

    public FuenteEstatica(ILector lector, String rutaDataset) {
        this.rutaDataset = rutaDataset;
        this.procesada = false;
        this.lector = lector;
        this.tipoFuente = TipoFuente.ESTATICA;
    }

    @Override
    public  List<Hecho> obtenerHechos() {
        List<Hecho> hechos = lector.obtencionHechos(this.rutaDataset);
        this.setProcesada(true);
        return hechos;
    };

    public void setProcesada(Boolean procesada) {
        this.procesada = procesada;
    }

    public Boolean getProcesada() {
        return procesada;
    }

    public String getRutaDataset(){
        return rutaDataset;
    }

}
