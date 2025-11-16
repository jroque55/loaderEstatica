package org.example.models.entities.fuenteEstatica;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.utils.EstadoProcesado;

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

    public void seleccionarLector() {
        if (rutaDataset.endsWith(".csv")) {
            this.lector = new LectorCSV();
        } if(rutaDataset.endsWith(".pdf")){
            this.lector =  new LectorPDF();
        }
    }
}
