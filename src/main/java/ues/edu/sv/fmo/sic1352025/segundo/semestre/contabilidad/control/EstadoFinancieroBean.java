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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.EstadoFinanciero;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for EstadoFinanciero entity operations (Financial Statements).
 */
@Stateless
@LocalBean
public class EstadoFinancieroBean extends AbstractDataPersist<EstadoFinanciero> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public EstadoFinancieroBean() {
        super(EstadoFinanciero.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all financial statements for a specific period
     * @param periodo accounting period
     * @return list of financial statements
     */
    public List<EstadoFinanciero> findByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<EstadoFinanciero> q = em.createNamedQuery("EstadoFinanciero.findByPeriodo", EstadoFinanciero.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find financial statements by type
     * @param tipo statement type (BALANCE_GENERAL, ESTADO_RESULTADOS)
     * @return list of financial statements
     */
    public List<EstadoFinanciero> findByTipo(String tipo) {
        try {
            TypedQuery<EstadoFinanciero> q = em.createNamedQuery("EstadoFinanciero.findByTipo", EstadoFinanciero.class);
            q.setParameter("tipo", tipo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find financial statements by period and type
     * @param periodo accounting period
     * @param tipo statement type
     * @return list of financial statements
     */
    public List<EstadoFinanciero> findByPeriodoYTipo(PeriodoContable periodo, String tipo) {
        try {
            TypedQuery<EstadoFinanciero> q = em.createNamedQuery("EstadoFinanciero.findByPeriodoYTipo", EstadoFinanciero.class);
            q.setParameter("periodo", periodo);
            q.setParameter("tipo", tipo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
