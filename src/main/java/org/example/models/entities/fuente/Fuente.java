package org.example.models.entities.fuente;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.utils.EnumTipoFuente;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Fuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idFuente;
    @Column(nullable = false)
    private String nombre;
    @Column(nullable = false)
    private String url;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnumTipoFuente tipoFuente;
}