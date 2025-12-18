package org.example.controller;

import jakarta.websocket.server.PathParam;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.hecho.Hecho;
import org.example.service.ServiceFuenteEstatica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/estatica")
public class ControllerFuenteEstatica {

    @Autowired
    private final ServiceFuenteEstatica serviceEstatica;

    public ControllerFuenteEstatica(ServiceFuenteEstatica serviceEstatica) {
        this.serviceEstatica = serviceEstatica;
    }

    @GetMapping("/fuentes")
    public ResponseEntity<List<FuenteEstatica>> findByLeidas(){
        List<FuenteEstatica> fe = this.serviceEstatica.findByLeidas();
        return ResponseEntity.status(200).body(fe);
    }

    @PostMapping(value = "/subirDataSet", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<String> subirArchivo(
             @RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("El archivo está vacío");
        }
        try {
            Boolean valor = serviceEstatica.subirDataSet(file);
            if (!valor){
                return ResponseEntity.internalServerError().body("Ese tipo de archivo no es soportado");
            }
            return ResponseEntity.ok("Archivo subido exitosamente: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al subir el archivo: " + e.getMessage());
        }
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<List<Hecho>>> getHechosNoLeidos() {
        List<List<Hecho>> hechos = this.serviceEstatica.leerDataSetNoLeidos();
        if( hechos == null || hechos.isEmpty()){
            return ResponseEntity.status(204).body(new ArrayList<>());
        }
        return ResponseEntity.status(200).body(hechos);
    }

    @PostMapping("/reprocesar/{fuente}")
    public ResponseEntity<String> reprocesarFuente(@PathVariable String fuente) {
        this.serviceEstatica.reprocesarFuente(fuente);
        return ResponseEntity.status(204).body("OK");
    }

}