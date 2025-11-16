package org.example.controller;

import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.models.entities.hecho.Hecho;
import org.example.service.ServiceFuenteEstatica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/{ruta}")
    public ResponseEntity<List<Hecho>> getHechosRuta(@PathVariable String ruta) {
        List<Hecho> hechos = this.serviceEstatica.leerDataSet(ruta);
        return ResponseEntity.status(200).body(hechos);
    }

    @GetMapping("/hechos")
    public ResponseEntity<List<Hecho>> getHechosNoLeidos() {
        List<Hecho> hechos = this.serviceEstatica.leerDataSetNoLeidos();
        return ResponseEntity.status(200).body(hechos);
    }


    @GetMapping("/fuentes/noleidas")
    public ResponseEntity<List<FuenteEstatica>> findByNoLeidas() {
        List<FuenteEstatica> fe = this.serviceEstatica.findByNoLeidas();
        return ResponseEntity.status(200).body(fe);
    }


}