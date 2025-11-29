package org.example.models.repository.repoAgregador;

import org.example.models.entities.fuente.Fuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRepositoryAgregador extends JpaRepository<Fuente, Long> {

    Fuente findByUrl(String url);

}
