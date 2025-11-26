package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for PeriodoContable entity operations.
 */
@Stateless
@LocalBean
public class PeriodoContableBean extends AbstractDataPersist<PeriodoContable> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public PeriodoContableBean() {
        super(PeriodoContable.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find the current active and open accounting period
     * @return active period or null if none found
     */
    public PeriodoContable findPeriodoActivo() {
        try {
            TypedQuery<PeriodoContable> q = em.createNamedQuery("PeriodoContable.findPeriodoActivo", PeriodoContable.class);
            q.setMaxResults(1);
            List<PeriodoContable> results = q.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find all active periods
     * @param activo active status
     * @return list of periods
     */
    public List<PeriodoContable> findByActivo(Boolean activo) {
        try {
            TypedQuery<PeriodoContable> q = em.createNamedQuery("PeriodoContable.findByActivo", PeriodoContable.class);
            q.setParameter("activo", activo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
