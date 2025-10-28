package org.example.models.repository;

import org.example.models.Schemas.fuente_estatica;
import org.example.models.entities.fuenteEstatica.FuenteEstatica;
import org.springframework.stereotype.Repository;
import org.example.utils.BDUtils;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import java.util.ArrayList;
import java.util.List;

@Repository
public class RepositoryFuenteEstatica {
    private final EntityManager em = BDUtils.getEntityManager();

    public void save(FuenteEstatica fuente) {
        fuente_estatica fes = toDTO(fuente);
        em.getTransaction().begin();
        em.persist(fes);
        em.getTransaction().commit();
    }

    public FuenteEstatica findByRuta(String ruta) {
        try {
            fuente_estatica fe = em.createQuery("FROM fuente_estatica f WHERE f.ruta = :ruta", fuente_estatica.class)
                    .setParameter("ruta", ruta)
                    .getSingleResult();
            return fromDTO(fe);
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<FuenteEstatica> findByLeidas() {
        try{
            List<fuente_estatica> lfes = em.createQuery("SELECT f FROM fuente_estatica f where f.estadoProcesado = PROCESADO", fuente_estatica.class).getResultList();
            return fromDTOs(lfes);
        } catch(NoResultException e) {
            return null;
        }
    }

    public FuenteEstatica fromDTO(fuente_estatica fes) {
        return new FuenteEstatica(fes.getRuta(),fes.getEstadoProcesado());
    }

    public List<FuenteEstatica> fromDTOs(List<fuente_estatica> lfes) {
        List<FuenteEstatica> lfe = new ArrayList<>();
        for(fuente_estatica fes : lfes){
            lfe.add(fromDTO(fes));
        }
        return lfe;
    }

    public fuente_estatica toDTO(FuenteEstatica fuente) {
        fuente_estatica fes = new fuente_estatica();
        fes.setRuta(fuente.getRutaDataset());
        fes.setEstadoProcesado(fuente.getEstado());
        return fes;
    }

}
