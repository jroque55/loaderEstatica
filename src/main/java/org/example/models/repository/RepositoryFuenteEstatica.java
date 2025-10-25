package org.example.models.repository;

import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.springframework.stereotype.Repository;
import org.example.utils.BDUtils;
import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryFuenteEstatica {
    private final List<FuenteEstatica> fuentes = new ArrayList<>();


    public FuenteEstatica findByRuta(String ruta) {
        for (FuenteEstatica fuente : fuentes) {
            if (fuente.getRutaDataset().equals(ruta)) {
                return fuente;
            }
        }
        return null;
    }

    public List<FuenteEstatica> findByLeidas() {
        List<FuenteEstatica> leidas = new ArrayList<>();
        for (FuenteEstatica fuente : fuentes) {
            if (fuente.getProcesada()) {
                leidas.add(fuente);
            }
        }
        return leidas;
    }

    public List<FuenteEstatica> findAll(){
        return fuentes;
    }

    public void save(FuenteEstatica fuente) {
        FuenteEstatica existente = findByRuta(fuente.getRutaDataset());
        if(existente != null) {
            fuentes.add(fuente);
        }
    }

}
