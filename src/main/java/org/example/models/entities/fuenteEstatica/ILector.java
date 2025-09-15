package org.example.models.entities.fuenteEstatica;

import java.util.List;
import org.example.models.entities.hecho.Hecho;

public interface ILector {

    public List<Hecho> obtencionHechos(String ruta);
}

