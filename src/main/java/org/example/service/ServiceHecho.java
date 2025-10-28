package org.example.service;

import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.ILector;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.fuenteEstatica.LectorPDF;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;

@Service
public class ServiceHecho {

    private RepositoryFuenteEstatica repositorioFuenteEstatica;

    public ServiceHecho(RepositoryFuenteEstatica repo) {
        this.repositorioFuenteEstatica = repo;
    }

    @Transactional
    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = findByRuta(ruta);
        if(fuente != null) {
            return fuente.obtenerHechos();
        } else {
            fuente = new FuenteEstatica(ruta, EstadoProcesado.NO_PROCESADO);
            List<Hecho> hechos = fuente.obtenerHechos();
            if(hechos == null) {
                fuente.setEstado(EstadoProcesado.PROCESADO);
            }
            repositorioFuenteEstatica.save(fuente);
            return hechos;
        }
    }

    public FuenteEstatica findByRuta(String ruta) {
        FuenteEstatica fe = repositorioFuenteEstatica.findByRuta(ruta);
        if(fe != null){
            return fe;
        }
        return null;
    }

    public List<FuenteEstatica> findByLeidas() {
        return this.repositorioFuenteEstatica.findByLeidas();
    }

}