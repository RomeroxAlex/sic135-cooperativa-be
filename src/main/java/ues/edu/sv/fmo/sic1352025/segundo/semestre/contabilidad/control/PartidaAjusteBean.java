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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaAjuste;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for PartidaAjuste entity operations (Adjustment Entries).
 */
@Stateless
@LocalBean
public class PartidaAjusteBean extends AbstractDataPersist<PartidaAjuste> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public PartidaAjusteBean() {
        super(PartidaAjuste.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all adjustment entries for a specific period
     * @param periodo accounting period
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<PartidaAjuste> q = em.createNamedQuery("PartidaAjuste.findByPeriodo", PartidaAjuste.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find adjustment entries by type
     * @param tipo adjustment type
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByTipo(String tipo) {
        try {
            TypedQuery<PartidaAjuste> q = em.createNamedQuery("PartidaAjuste.findByTipo", PartidaAjuste.class);
            q.setParameter("tipo", tipo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find adjustment entries by status
     * @param estado adjustment status
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByEstado(String estado) {
        try {
            TypedQuery<PartidaAjuste> q = em.createNamedQuery("PartidaAjuste.findByEstado", PartidaAjuste.class);
            q.setParameter("estado", estado);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find all pending adjustment entries
     * @return list of pending adjustment entries
     */
    public List<PartidaAjuste> findPendientes() {
        try {
            TypedQuery<PartidaAjuste> q = em.createNamedQuery("PartidaAjuste.findPendientes", PartidaAjuste.class);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find adjustment entries by automatic flag
     * @param automatico true for automatic, false for manual
     * @return list of adjustment entries
     */
    public List<PartidaAjuste> findByAutomatico(Boolean automatico) {
        try {
            TypedQuery<PartidaAjuste> q = em.createNamedQuery("PartidaAjuste.findByAutomatico", PartidaAjuste.class);
            q.setParameter("automatico", automatico);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
