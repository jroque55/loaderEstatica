package org.example.models.Schemas;
import javax.persistence.*;
import org.example.utils.EstadoProcesado;

@Entity
public class fuente_estatica {

    @Id
    @Column(unique = true)
    private String ruta;

    @Enumerated(EnumType.STRING)
    private EstadoProcesado estadoProcesado;

    public String getRuta() { return ruta; }
    public void setRuta(String ruta) { this.ruta = ruta; }

    public EstadoProcesado getEstadoProcesado() { return estadoProcesado; }
    public void setEstadoProcesado(EstadoProcesado estadoProcesado) { this.estadoProcesado = estadoProcesado; }

}
