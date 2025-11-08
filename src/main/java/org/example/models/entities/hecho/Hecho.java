package org.example.models.entities.hecho;


import org.example.models.dtos.FuenteDto;
import org.example.models.entities.fuenteEstatica.Fuente;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Hecho {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private LocalDate fecha;
    private Ubicacion lugarDeOcurrencia;
    private FuenteDto fuente;

    public Hecho(String titulo, String descripcion, Categoria categoria,Ubicacion lugarDeOcurrencia, LocalDate fecha, FuenteDto fuente) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.lugarDeOcurrencia = lugarDeOcurrencia;
        this.fecha = fecha;
        this.fuente = fuente;
    }
}
