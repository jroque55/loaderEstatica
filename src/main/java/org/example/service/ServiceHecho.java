package org.example.service;


import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.ILector;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.fuenteEstatica.LectorPDF;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.lang.String;

@Service
public class ServiceHecho {

    private RepositoryFuenteEstatica repositorioFuenteEstatica;

    public ServiceHecho(RepositoryFuenteEstatica repo) {
        this.repositorioFuenteEstatica = repo;
    }

    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = findByRuta(ruta);
        if(fuente != null) {
            return fuente.obtenerHechos();
        } else {
            ILector lector = seleccionarLector(ruta);
            fuente = new FuenteEstatica(lector, ruta);
            return fuente.obtenerHechos();
        }
    }

    public FuenteEstatica findByRuta(String ruta) {
        return repositorioFuenteEstatica.findByRuta(ruta);
    }

    public List<FuenteEstatica> findByLeidas() {
        //TODO agregar un try catch despues, cuando agreguemos orm
        return this.repositorioFuenteEstatica.findByLeidas();
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