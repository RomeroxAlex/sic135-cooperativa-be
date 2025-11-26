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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.BalanzaComprobacion;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for BalanzaComprobacion entity operations (Trial Balance).
 */
@Stateless
@LocalBean
public class BalanzaComprobacionBean extends AbstractDataPersist<BalanzaComprobacion> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public BalanzaComprobacionBean() {
        super(BalanzaComprobacion.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all trial balance entries for a specific period
     * @param periodo accounting period
     * @return list of trial balance entries
     */
    public List<BalanzaComprobacion> findByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<BalanzaComprobacion> q = em.createNamedQuery("BalanzaComprobacion.findByPeriodo", BalanzaComprobacion.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Delete all trial balance entries for a period (to regenerate)
     * @param periodo accounting period
     */
    public void deleteByPeriodo(PeriodoContable periodo) {
        try {
            em.createQuery("DELETE FROM BalanzaComprobacion b WHERE b.idPeriodoContable = :periodo")
              .setParameter("periodo", periodo)
              .executeUpdate();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
