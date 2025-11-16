package org.example.models.repository.repoFuenteEstatica;

import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.example.utils.EstadoProcesado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IRepositoryFuenteEstatica extends JpaRepository<FuenteEstatica, String> {

    FuenteEstatica findByRutaDataset(String ruta);

    List<FuenteEstatica> findByEstadoProcesado(EstadoProcesado estadoProcesado);
}
