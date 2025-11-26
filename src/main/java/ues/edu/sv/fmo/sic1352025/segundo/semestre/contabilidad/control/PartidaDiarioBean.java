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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for PartidaDiario entity operations (Journal Book).
 */
@Stateless
@LocalBean
public class PartidaDiarioBean extends AbstractDataPersist<PartidaDiario> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public PartidaDiarioBean() {
        super(PartidaDiario.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all journal entries for a specific period
     * @param periodo accounting period
     * @return list of journal entries
     */
    public List<PartidaDiario> findByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<PartidaDiario> q = em.createNamedQuery("PartidaDiario.findByPeriodo", PartidaDiario.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find journal entries by type (NORMAL, AJUSTE, CIERRE, APERTURA)
     * @param tipo entry type
     * @return list of journal entries
     */
    public List<PartidaDiario> findByTipo(String tipo) {
        try {
            TypedQuery<PartidaDiario> q = em.createNamedQuery("PartidaDiario.findByTipo", PartidaDiario.class);
            q.setParameter("tipo", tipo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find journal entries by status
     * @param estado entry status
     * @return list of journal entries
     */
    public List<PartidaDiario> findByEstado(String estado) {
        try {
            TypedQuery<PartidaDiario> q = em.createNamedQuery("PartidaDiario.findByEstado", PartidaDiario.class);
            q.setParameter("estado", estado);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Get the maximum entry number for a period
     * @param periodo accounting period
     * @return max number or null
     */
    public Integer findMaxNumeroByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<Integer> q = em.createNamedQuery("PartidaDiario.findMaxNumeroByPeriodo", Integer.class);
            q.setParameter("periodo", periodo);
            return q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
