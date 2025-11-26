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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.LibroMayor;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for LibroMayor entity operations (General Ledger).
 */
@Stateless
@LocalBean
public class LibroMayorBean extends AbstractDataPersist<LibroMayor> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public LibroMayorBean() {
        super(LibroMayor.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all ledger entries for a specific period
     * @param periodo accounting period
     * @return list of ledger entries
     */
    public List<LibroMayor> findByPeriodo(PeriodoContable periodo) {
        try {
            TypedQuery<LibroMayor> q = em.createNamedQuery("LibroMayor.findByPeriodo", LibroMayor.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find all ledger entries for a specific account
     * @param cuenta accounting account
     * @return list of ledger entries
     */
    public List<LibroMayor> findByCuenta(CuentaContable cuenta) {
        try {
            TypedQuery<LibroMayor> q = em.createNamedQuery("LibroMayor.findByCuenta", LibroMayor.class);
            q.setParameter("cuenta", cuenta);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find ledger entry for a specific account and period
     * @param cuenta accounting account
     * @param periodo accounting period
     * @return ledger entry or null
     */
    public LibroMayor findByCuentaYPeriodo(CuentaContable cuenta, PeriodoContable periodo) {
        try {
            TypedQuery<LibroMayor> q = em.createNamedQuery("LibroMayor.findByCuentaYPeriodo", LibroMayor.class);
            q.setParameter("cuenta", cuenta);
            q.setParameter("periodo", periodo);
            List<LibroMayor> results = q.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find ledger entries with non-zero balances for a period
     * @param periodo accounting period
     * @return list of ledger entries with balances
     */
    public List<LibroMayor> findByPeriodoConSaldo(PeriodoContable periodo) {
        try {
            TypedQuery<LibroMayor> q = em.createNamedQuery("LibroMayor.findByPeriodoConSaldo", LibroMayor.class);
            q.setParameter("periodo", periodo);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
