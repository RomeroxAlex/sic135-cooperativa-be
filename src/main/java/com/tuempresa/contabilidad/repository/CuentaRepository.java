package com.tuempresa.contabilidad.repository;

import com.tuempresa.contabilidad.entity.Cuenta;
import com.tuempresa.contabilidad.entity.TipoCuenta;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class CuentaRepository {

    @PersistenceContext(unitName = "contabilidadPU")
    private EntityManager em;

    public List<Cuenta> findWithFilters(int page, int size, TipoCuenta tipo) {
        String qlString = "SELECT c FROM Cuenta c WHERE (:tipo IS NULL OR c.tipo = :tipo)";
        var query = em.createQuery(qlString, Cuenta.class);
        query.setParameter("tipo", tipo);
        query.setFirstResult((page - 1) * size);
        query.setMaxResults(size);
        return query.getResultList();
    }

    public long countWithFilters(TipoCuenta tipo) {
        String qlString = "SELECT COUNT(c) FROM Cuenta c WHERE (:tipo IS NULL OR c.tipo = :tipo)";
        var query = em.createQuery(qlString, Long.class);
        query.setParameter("tipo", tipo);
        return query.getSingleResult();
    }

    public Optional<Cuenta> findById(Long id) {
        return Optional.ofNullable(em.find(Cuenta.class, id));
    }

    public Optional<Cuenta> findByCodigo(String codigo) {
        return em.createQuery("SELECT c FROM Cuenta c WHERE c.codigo = :codigo", Cuenta.class)
                .setParameter("codigo", codigo)
                .getResultStream()
                .findFirst();
    }

    public void save(Cuenta cuenta) {
        em.persist(cuenta);
    }

    public Cuenta update(Cuenta cuenta) {
        return em.merge(cuenta);
    }

    public void delete(Cuenta cuenta) {
        if (em.contains(cuenta)) {
            em.remove(cuenta);
        } else {
            em.remove(em.merge(cuenta));
        }
    }
}
