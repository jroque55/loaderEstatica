package org.example.models.dtos;

import org.example.models.entities.fuenteEstatica.Fuente;
import org.example.models.entities.hecho.Categoria;
import org.example.models.entities.hecho.Ubicacion;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HechoDto {
    private String titulo;
    private String descripcion;
    private Categoria categoria;
    private LocalDate fecha;
    private Ubicacion lugarDeOcurrencia;
    private Fuente origen;

}
