package org.example.service;

import lombok.extern.slf4j.Slf4j;
import org.example.models.dtos.FuenteDto;
import org.example.models.dtos.FuenteEstaticaDTO;
import org.example.models.entities.fuente.Fuente;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.fuenteEstatica.LectorCSV;
import org.example.models.entities.hecho.Hecho;
import org.example.models.repository.repoAgregador.IRepositoryAgregador;
import org.example.models.repository.repoFuenteEstatica.IRepositoryFuenteEstatica;
import org.example.utils.EnumTipoFuente;
import org.example.utils.EstadoProcesado;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.lang.String;
import java.util.stream.Stream;
import java.nio.file.Paths;

@Slf4j
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

    public List<FuenteEstaticaDTO> findAll() {
        log.info("Obteniendo todas las fuentes estáticas");
        List<FuenteEstatica> fes = this.repositoryFuenteEstatica.findAll();
        if (fes == null || fes.isEmpty()) {
            log.debug("No se encontraron fuentes estáticas");
            return new ArrayList<>();
        }
        log.debug("Fuentes estáticas obtenidas: {}", fes.size());
        List<FuenteEstaticaDTO> fuenteDtos = pasarFuenteEstaticaDTO(fes);
        return fuenteDtos;
    }

    private List<FuenteEstaticaDTO> pasarFuenteEstaticaDTO(List<FuenteEstatica> fes) {
        log.debug("Convirtiendo {} de FuenteEstatica a DTO", fes.size());
        List<FuenteEstaticaDTO> fuenteDtos = new ArrayList<>();
        for (FuenteEstatica fe : fes) {
            FuenteEstaticaDTO feDTO = new FuenteEstaticaDTO(fe);
            fuenteDtos.add(feDTO);
        }
        return fuenteDtos;
    }


    public Boolean subirDataSet(MultipartFile file) throws IOException {

        Path rootLocation = Paths.get(this.urlFile);

        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
        Path destinationFile = rootLocation.resolve(
                        Paths.get(file.getOriginalFilename()))
                .normalize().toAbsolutePath();

        if (!destinationFile.getParent().equals(rootLocation.toAbsolutePath())) {
            throw new IOException("No se puede guardar el archivo fuera del directorio configurado.");
        }
        if(!destinationFile.endsWith(".csv") || !destinationFile.endsWith(".pdf")){
            System.out.println("Ese tipo de archivo no corresponde a un dataset");
            return false;
        }

        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Archivo guardado físicamente en: " + destinationFile);
            return true;
        }

    }

    @Transactional
    public List<Hecho> leerDataSet(FuenteEstatica fuenteEstatica){
        seleccionarLectorAFuente(fuenteEstatica);
        log.info("Leyendo dataset de la fuente: {}", fuenteEstatica.getRutaDataset());
        List<Hecho> hechos = fuenteEstatica.obtenerHechos();
        fuenteEstatica.setEstadoProcesado(EstadoProcesado.PROCESADO);
        log.info("Hechos obtenidos: {}. Marcando fuente como procesada.", hechos.size());
        this.repositoryFuenteEstatica.save(fuenteEstatica);
        return hechos;
    }

    @Scheduled(fixedRate = 3600000)
    @Transactional
    public void buscarNuevasFuentes(){
        Path dirPath = Paths.get(this.urlFile);
        if (!Files.isDirectory(dirPath)) {
            throw new RuntimeException("Error: La ruta especificada no es un directorio válido.");
        }
        try (Stream<Path> stream = Files.list(dirPath)) {
            log.info("Buscando nuevas fuentes en el directorio: {}", this.urlFile);
            List<String> archivos = stream.filter(Files::isRegularFile)
                    .map(Path::getFileName).map(Path::toString).toList();
            for(String archivo : archivos){
                log.info("Archivo encontrado: {}", archivo);
                FuenteEstatica fe = this.repositoryFuenteEstatica.findByRutaDataset(archivo);
                if(fe == null) {
                    log.info("Agreando nueva fuente estática {}", archivo);
                    fe = new FuenteEstatica(archivo);
                    fe.setEstadoProcesado(EstadoProcesado.NO_PROCESADO);
                    repositoryFuenteEstatica.save(fe);
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
            return new ArrayList<>();
        }
        log.debug("Fuentes no leídas encontradas: {}", fuentesNoLeidas.size());
        List<List<Hecho>> hechosTotales = new ArrayList<>();
        for (FuenteEstatica fuente : fuentesNoLeidas) {
            log.info("Procesando fuente no leída: {}", fuente.getRutaDataset());
            List<Hecho> hechos = this.leerDataSet(fuente);
            hechosTotales.add(hechos);
        }
        return hechosTotales;
    }

    @Transactional
    public void reprocesarFuente(String ruta) {
        FuenteEstatica fuente = this.repositoryFuenteEstatica.findByRutaDataset(ruta);
        if(fuente == null){
            throw new RuntimeException("No se encontro la fuente estatica con la ruta: " + ruta);
        }
        fuente.setEstadoProcesado(EstadoProcesado.NO_PROCESADO);
        this.repositoryFuenteEstatica.save(fuente);
    }

    @Transactional
    @Scheduled(fixedRate = 10000000)
    public void subirFuentesAlAgregador() {
        log.info("Iniciando proceso de subir fuentes no leídas al agregador");
        List<FuenteEstatica> fuentesNoLeidas = this.findByNoLeidas();
        if(fuentesNoLeidas == null || fuentesNoLeidas.isEmpty()) {
            throw new RuntimeException("No hay fuentes no leidas para subir al agregador.");
        }
        for (FuenteEstatica fuente : fuentesNoLeidas) {
            log.debug("Subiendo nueva fuente estática al agregador: {}", fuente.getRutaDataset());
            Fuente esNueva = this.repositoryAgregador.findByUrl(fuente.getRutaDataset());
            if(esNueva == null) {
                Fuente f = new Fuente();
                f.setNombre(fuente.getNombre());
                f.setTipoFuente(EnumTipoFuente.ESTATICA);
                f.setUrl(fuente.getRutaDataset());
                this.repositoryAgregador.save(f);
            }else {
                log.warn("Esta fuente ya existe en el agregador, no se subirá nuevamente: {}", fuente.getRutaDataset());
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