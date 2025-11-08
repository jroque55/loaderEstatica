package org.example.models.entities.fuenteEstatica;

import java.util.List;
import org.example.models.entities.hecho.Hecho;

public abstract class Fuente {

    private String ruta;
    private TipoFuente tipoFuente;

    public Fuente(String ruta, TipoFuente tipoFuente) {
        this.ruta = ruta;
        this.tipoFuente = tipoFuente;
    }

    public abstract List<Hecho> obtenerHechos(String rutaBase);

}