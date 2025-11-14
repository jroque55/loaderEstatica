package org.example.models.Schemas;
import javax.persistence.*;

import lombok.Data;
import org.example.models.entities.fuenteEstatica.TipoFuente;
import org.example.utils.EstadoProcesado;

@Data
@Entity
public class fuente_estatica {

    @Id
    @Column(unique = true)
    private String ruta;
    private String nombre;
    @Enumerated(EnumType.STRING)
    private EstadoProcesado estadoProcesado;
    @Enumerated(EnumType.STRING)
    private TipoFuente tipoFuente = TipoFuente.ESTATICA;
}
