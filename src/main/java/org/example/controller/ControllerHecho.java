package org.example.controller;

import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.hecho.Hecho;
import org.example.service.ServiceHecho;
import org.example.models.dtos.HechoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/estatica")
public class ControllerHecho {

    @Value("${app.urlFile}")
    private String urlFile;
    @Autowired
    private ServiceHecho serviceHecho;

    public ControllerHecho(ServiceHecho serviceHecho) {
        this.serviceHecho = serviceHecho;
    }

    public void setUrlFile(String ruta) {
        this.urlFile = ruta;
    }

    @GetMapping("/fuentes")
    public ResponseEntity<List<FuenteEstatica>> findByLeidas() {
        List<FuenteEstatica> fe = this.serviceHecho.findByLeidas();
        return ResponseEntity.status(200).body(fe);
    }

    @GetMapping("/{ruta}")
    public ResponseEntity<List<Hecho>> getHechosRuta(@PathVariable String ruta) {
        List<Hecho> hechos = this.serviceHecho.leerDataSet(this.urlFile + "/" + ruta);
        return ResponseEntity.status(200).body(hechos);
    }
}