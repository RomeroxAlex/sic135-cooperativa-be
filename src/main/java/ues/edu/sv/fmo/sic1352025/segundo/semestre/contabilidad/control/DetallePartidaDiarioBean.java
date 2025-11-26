package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetallePartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PartidaDiario;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.PeriodoContable;

/**
 * Bean for DetallePartidaDiario entity operations.
 */
@Stateless
@LocalBean
public class DetallePartidaDiarioBean extends AbstractDataPersist<DetallePartidaDiario> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public DetallePartidaDiarioBean() {
        super(DetallePartidaDiario.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find all detail lines for a journal entry
     * @param partida journal entry
     * @return list of detail lines
     */
    public List<DetallePartidaDiario> findByPartida(PartidaDiario partida) {
        try {
            TypedQuery<DetallePartidaDiario> q = em.createNamedQuery("DetallePartidaDiario.findByPartida", DetallePartidaDiario.class);
            q.setParameter("partida", partida);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find all detail lines for a specific account
     * @param cuenta accounting account
     * @return list of detail lines
     */
    public List<DetallePartidaDiario> findByCuenta(CuentaContable cuenta) {
        try {
            TypedQuery<DetallePartidaDiario> q = em.createNamedQuery("DetallePartidaDiario.findByCuenta", DetallePartidaDiario.class);
            q.setParameter("cuenta", cuenta);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Sum debit movements for an account in a period
     * @param cuenta accounting account
     * @param periodo accounting period
     * @return sum of debit amounts
     */
    public BigDecimal sumDebeByPeriodoYCuenta(CuentaContable cuenta, PeriodoContable periodo) {
        try {
            TypedQuery<BigDecimal> q = em.createNamedQuery("DetallePartidaDiario.sumDebeByPeriodoYCuenta", BigDecimal.class);
            q.setParameter("cuenta", cuenta);
            q.setParameter("periodo", periodo);
            return q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
    
    /**
     * Sum credit movements for an account in a period
     * @param cuenta accounting account
     * @param periodo accounting period
     * @return sum of credit amounts
     */
    public BigDecimal sumHaberByPeriodoYCuenta(CuentaContable cuenta, PeriodoContable periodo) {
        try {
            TypedQuery<BigDecimal> q = em.createNamedQuery("DetallePartidaDiario.sumHaberByPeriodoYCuenta", BigDecimal.class);
            q.setParameter("cuenta", cuenta);
            q.setParameter("periodo", periodo);
            return q.getSingleResult();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return BigDecimal.ZERO;
        }
    }
}
