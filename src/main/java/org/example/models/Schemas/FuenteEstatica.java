package org.example.models.Schemas;
import javax.persistence.*;

@Entity
public class FuenteEstatica{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String ruta;
    private String


}
