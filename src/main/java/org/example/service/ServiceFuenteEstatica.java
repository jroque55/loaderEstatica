package org.example.service;


import org.example.models.entities.fuente.Fuente;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.fuenteEstatica.LectorPDF;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.repoAgregador.IRepositoryAgregador;
import org.example.models.repository.repoFuenteEstatica.IRepositoryFuenteEstatica;
import org.example.utils.EnumTipoFuente;
import org.example.utils.EstadoProcesado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.util.stream.Stream;
import java.nio.file.Paths;

@Service
public class ServiceFuenteEstatica {

    @Value("${app.urlFile}")
    private String urlFile;
    private final LectorCSV lectorCSV;
    private IRepositoryFuenteEstatica repositoryFuenteEstatica;
    private IRepositoryAgregador repositoryAgregador;

    public ServiceFuenteEstatica(LectorCSV lectorCSV, IRepositoryFuenteEstatica repositoryFuenteEstatica, IRepositoryAgregador repositoryAgregador) {
        this.lectorCSV = lectorCSV;
        this.repositoryFuenteEstatica = repositoryFuenteEstatica;
        this.repositoryAgregador = repositoryAgregador;
    }

    @Transactional
    public List<Hecho> leerDataSet(FuenteEstatica fuenteEstatica){
        seleccionarLectorAFuente(fuenteEstatica);
        List<Hecho> hechos = fuenteEstatica.obtenerHechos();
        fuenteEstatica.setEstadoProcesado(EstadoProcesado.PROCESADO);
        return hechos;
    }


    @Transactional
    public List<Hecho> leerDataSet(String ruta) {
        FuenteEstatica fuente = this.repositoryFuenteEstatica.findByRutaDataset(ruta);
        if(fuente == null) {
            throw new RuntimeException("No se encontro la fuente estatica con la ruta: " + ruta);
        } else {
            if(fuente.getEstadoProcesado() == EstadoProcesado.PROCESADO) {
                throw new RuntimeException("La fuente estatica con la ruta: " + ruta + " ya fue procesada anteriormente.");
            }
            seleccionarLectorAFuente(fuente);
            List<Hecho> hechos = fuente.obtenerHechos();
            fuente.setEstadoProcesado(EstadoProcesado.PROCESADO);
            this.repositoryFuenteEstatica.save(fuente);
            return hechos;
        }
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
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
                FuenteEstatica fe = this.repositoryFuenteEstatica.findByRutaDataset(archivo);
                if(fe == null) {
                    fe = new FuenteEstatica(archivo);
                    fe.setEstadoProcesado(EstadoProcesado.NO_PROCESADO);
                    repositoryFuenteEstatica.save(fe);
                    System.out.println("Nueva fuente estatica agregada: " + archivo);
                }
            }

        } catch (IOException e) {
            System.err.println("Error al leer el directorio: " + e.getMessage());
        }
    }

    public List<FuenteEstatica> findByLeidas() {
        return this.repositoryFuenteEstatica.findByEstadoProcesado(EstadoProcesado.PROCESADO);
    }

    public List<FuenteEstatica> findByNoLeidas() {
        return this.repositoryFuenteEstatica.findByEstadoProcesado(EstadoProcesado.NO_PROCESADO);
    }

    @Transactional
    public List<List<Hecho>> leerDataSetNoLeidos() {
        List<FuenteEstatica> fuentesNoLeidas = this.findByNoLeidas();
        if(fuentesNoLeidas == null || fuentesNoLeidas.isEmpty()) {
            throw new RuntimeException("No hay fuentes no leidas.");
        }
        List<List<Hecho>> hechosTotales = new ArrayList<>();
        for (FuenteEstatica fuente : fuentesNoLeidas) {
            System.out.println("Leyendo fuente no leida: " + fuente.getRutaDataset());
            List<Hecho> hechos = this.leerDataSet(fuente);
            hechosTotales.add(hechos);
        }
        return hechosTotales;
    }

    @Transactional
    @Scheduled(fixedRate = 10000000)
    public void subirFuentesAlAgregador() {
        List<FuenteEstatica> fuentesNoLeidas = this.findByNoLeidas();
        if(fuentesNoLeidas == null || fuentesNoLeidas.isEmpty()) {
            throw new RuntimeException("No hay fuentes no leidas para subir al agregador.");
        }
        for (FuenteEstatica fuente : fuentesNoLeidas) {
            //TODO: Chequear que no se suba siempre al agregador si ya existe
            Fuente esNueva = this.repositoryAgregador.findByUrl(fuente.getRutaDataset());
            if(esNueva == null) {
                Fuente f = new Fuente();
                f.setNombre(fuente.getNombre());
                f.setTipoFuente(EnumTipoFuente.ESTATICA);
                f.setUrl(fuente.getRutaDataset());
                System.out.println("Nueva fuente: " + f.getUrl() );
                this.repositoryAgregador.save(f);
            }else {
                System.out.println("Fuente existe, no se agrego: "+ esNueva.getUrl() );
            }
        }
    }

    public void seleccionarLectorAFuente(FuenteEstatica fuente) {
        if (fuente.getRutaDataset().endsWith(".csv")) {
            fuente.lector = this.lectorCSV;
        } if(fuente.getRutaDataset().endsWith(".pdf")){
            //fuente.lector =  new LectorPDF();
        }
    }

}