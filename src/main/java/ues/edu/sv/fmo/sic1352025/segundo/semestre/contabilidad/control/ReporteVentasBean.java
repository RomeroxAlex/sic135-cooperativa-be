package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.ReporteVentas;

/**
 * Bean for ReporteVentas entity operations (Daily Sales Report).
 */
@Stateless
@LocalBean
public class ReporteVentasBean extends AbstractDataPersist<ReporteVentas> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public ReporteVentasBean() {
        super(ReporteVentas.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find sales report by date
     * @param fecha report date
     * @return sales report or null
     */
    public ReporteVentas findByFecha(Date fecha) {
        try {
            TypedQuery<ReporteVentas> q = em.createNamedQuery("ReporteVentas.findByFecha", ReporteVentas.class);
            q.setParameter("fecha", fecha);
            List<ReporteVentas> results = q.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find sales reports by date range
     * @param fechaInicio start date
     * @param fechaFin end date
     * @return list of sales reports
     */
    public List<ReporteVentas> findByRangoFechas(Date fechaInicio, Date fechaFin) {
        try {
            TypedQuery<ReporteVentas> q = em.createNamedQuery("ReporteVentas.findByRangoFechas", ReporteVentas.class);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
