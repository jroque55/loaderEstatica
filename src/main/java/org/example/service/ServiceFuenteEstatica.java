package org.example.service;

import org.example.models.Schemas.fuente_estatica;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.RepositoryFuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.lang.String;
import java.util.stream.Stream;
import java.nio.file.Paths;

@Service
public class ServiceFuenteEstatica {

    @Value("${app.urlFile}")
    private String urlFile;
    private final LectorCSV lectorCSV;
    private final RepositoryFuenteEstatica repositorioFuenteEstatica;

    public ServiceFuenteEstatica(RepositoryFuenteEstatica repo, LectorCSV lectorCSV) {
        this.repositorioFuenteEstatica = repo;
        this.lectorCSV = lectorCSV;
    }

    @Transactional
    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = this.findByRuta(ruta);
        if(fuente == null) {
            throw new RuntimeException("No se encontro la fuente estatica con la ruta: " + ruta);
        } else {
            if(fuente.getEstadoProcesado() == EstadoProcesado.PROCESADO) {
                return lectorCSV.obtencionHechos(ruta);
            }
            fuente = new FuenteEstatica(ruta);
            List<Hecho> hechos = lectorCSV.obtencionHechos(ruta);
            fuente.setEstadoProcesado(EstadoProcesado.PROCESADO);
            repositorioFuenteEstatica.update(fuente);
            return hechos;
        }
    }

    @Scheduled(fixedRate = 360000)
    public void buscarNuevasFuentes(){
        Path dirPath = Paths.get(this.urlFile);
        if (!Files.isDirectory(dirPath)) {
            throw new RuntimeException("Error: La ruta especificada no es un directorio válido.");
        }
        try (Stream<Path> stream = Files.list(dirPath)) {

            System.out.println("--- Archivos en el directorio: " + this.urlFile + " ---");
            List<String> archivos = stream.filter(Files::isRegularFile)
                    .map(Path::getFileName).map(Path::toString).toList();
            for(String archivo : archivos){
                FuenteEstatica fe = this.findByRuta(archivo);
                if(fe == null){
                    fe = new FuenteEstatica(archivo);
                    fe.setEstadoProcesado(EstadoProcesado.NO_PROCESADO);
                    repositorioFuenteEstatica.save(fe);
                    System.out.println("Nueva fuente estatica agregada: " + archivo);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el directorio: " + e.getMessage());
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

    public List<fuente_estatica> findByNoLeidas() {
        return this.repositorioFuenteEstatica.findByNoLeidas();
    }


}