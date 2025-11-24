package com.tuempresa.contabilidad.repository;

import com.tuempresa.contabilidad.entity.ManualCuenta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class ManualCuentaRepository {

    @PersistenceContext(unitName = "contabilidadPU")
    private EntityManager em;

    public List<ManualCuenta> findAll() {
        return em.createQuery("SELECT m FROM ManualCuenta m", ManualCuenta.class).getResultList();
    }

    public Optional<ManualCuenta> findById(Long id) {
        return Optional.ofNullable(em.find(ManualCuenta.class, id));
    }

    public void save(ManualCuenta manual) {
        em.persist(manual);
    }

    public ManualCuenta update(ManualCuenta manual) {
        return em.merge(manual);
    }
}
