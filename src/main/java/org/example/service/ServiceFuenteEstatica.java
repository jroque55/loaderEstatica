package org.example.service;

import org.example.models.Schemas.fuente_estatica;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.lang.String;
import java.util.stream.Stream;

@Service
public class ServiceFuenteEstatica {

    private final LectorCSV lectorCSV;
    private RepositoryFuenteEstatica repositorioFuenteEstatica;

    public ServiceFuenteEstatica(RepositoryFuenteEstatica repo, LectorCSV lectorCSV) {
        this.repositorioFuenteEstatica = repo;this.lectorCSV = lectorCSV;
    }

    @Transactional
    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = this.findByRuta(ruta);
        if(fuente != null) {
            return lectorCSV.obtencionHechos(ruta);
        } else {
            fuente = new FuenteEstatica(ruta);
            List<Hecho> hechos = lectorCSV.obtencionHechos(ruta);
            if(hechos.isEmpty()) {
                fuente.setEstadoProcesado(EstadoProcesado.PROCESADO);
                throw new RuntimeException("No se pudieron obtener hechos de la fuente estatica");
            }
            fuente.setEstadoProcesado(EstadoProcesado.PROCESADO);
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

    public List<fuente_estatica> findByLeidas() {
        return this.repositorioFuenteEstatica.findByLeidas();
    }



}