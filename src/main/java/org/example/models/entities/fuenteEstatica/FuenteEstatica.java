package org.example.models.entities.fuenteEstatica;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.models.entities.hecho.Hecho;
import org.example.utils.EstadoProcesado;

import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class FuenteEstatica {

    @Id
    @Column(unique = true)
    public String rutaDataset;
    public String nombre;
    @Transient
    public ILector lector;
    @Enumerated(EnumType.STRING)
    public EstadoProcesado estadoProcesado;
    @Enumerated(EnumType.STRING)
    private TipoFuente tipoFuente = TipoFuente.ESTATICA;

    public FuenteEstatica(String rutaDataset) {
        this.rutaDataset = rutaDataset;
        this.nombre = this.rutaDataset.substring(0, rutaDataset.length() - 4);
        this.estadoProcesado = EstadoProcesado.NO_PROCESADO;
    }

    public List<Hecho> obtenerHechos() {
        return lector.obtencionHechos(this.rutaDataset);
    }

}
