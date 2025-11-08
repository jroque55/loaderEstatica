package org.example.service;

import org.example.models.Schemas.fuente_estatica;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.lang.String;

@Service
public class ServiceFuenteEstatica {

    @Value("${app.urlFile}")
    private String urlFile;
    private RepositoryFuenteEstatica repositorioFuenteEstatica;

    public ServiceFuenteEstatica(RepositoryFuenteEstatica repo) {
        this.repositorioFuenteEstatica = repo;
    }

    @Transactional
    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = findByRuta(ruta);
        if(fuente != null) {
            return fuente.obtenerHechos(this.urlFile);
        } else {
            fuente = new FuenteEstatica(ruta, EstadoProcesado.NO_PROCESADO);
            List<Hecho> hechos = fuente.obtenerHechos(this.urlFile);
            if(hechos == null) {
                fuente.setEstadoProcesado(EstadoProcesado.PROCESADO);
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

    public List<fuente_estatica> findByLeidas() {
        return this.repositorioFuenteEstatica.findByLeidas();
    }

}