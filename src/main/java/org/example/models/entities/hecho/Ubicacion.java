package org.example.models.entities.hecho;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Ubicacion {
    private float latitud;
    private float longitud;

    public Ubicacion(float v, float v1) {
        this.latitud = v;
        this.longitud = v1;
    }

    public void setUbicacion(float nuevaLatitud, float nuevaLongitud) {
        latitud = nuevaLatitud;
        longitud = nuevaLongitud;
    }
}