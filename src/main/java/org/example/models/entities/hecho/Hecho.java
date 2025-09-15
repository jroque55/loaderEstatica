package org.example.models.entities.hecho;


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
    private Fuente origen;

    public Hecho(String titulo, String descripcion, Categoria categoria,Ubicacion lugarDeOcurrencia, LocalDate fecha) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.lugarDeOcurrencia = lugarDeOcurrencia;
        this.fecha = fecha;
    }

    public String getTitulo() {return titulo;}
    public void setTitulo(String titulo) {this.titulo= titulo;}
    public void setDescripcion(String descripcion) {this.descripcion = descripcion;}
    public Categoria getCategoria() {return categoria;}
    public void setCategoria(String categoria) {this.categoria.setNombre(categoria);}

    //Así me vendría la info, en String?
    public void setUbicacion(String dato, String dato1) {
        float nuevaLatitud = Float.parseFloat(dato);
        float nuevaLongitud = Float.parseFloat(dato1);
        lugarDeOcurrencia.setUbicacion(nuevaLatitud,nuevaLongitud);
    }

    public LocalDate getFecha() {return fecha;}
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
    public Fuente getOrigen() {return origen;}

}
