package org.example.models.entities.hecho;


import org.example.models.dtos.FuenteDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private LocalDate fecha;
    private Float latitud;
    private Float longitud;
    private FuenteDto fuente;

    public Hecho(String titulo, String descripcion, Categoria categoria,Float latitud, Float longitud, LocalDate fecha, FuenteDto fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.fecha = fecha;
        this.fuente = fuente;
    }

    public Hecho() {

    }
}
