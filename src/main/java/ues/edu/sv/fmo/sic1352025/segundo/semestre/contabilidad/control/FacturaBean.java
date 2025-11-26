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
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Factura;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Socio;

/**
 * Bean for Factura entity operations (Invoices).
 */
@Stateless
@LocalBean
public class FacturaBean extends AbstractDataPersist<Factura> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public FacturaBean() {
        super(Factura.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
    
    /**
     * Find invoice by invoice number
     * @param numero invoice number
     * @return invoice or null
     */
    public Factura findByNumero(String numero) {
        try {
            TypedQuery<Factura> q = em.createNamedQuery("Factura.findByNumero", Factura.class);
            q.setParameter("numero", numero);
            List<Factura> results = q.getResultList();
            return results.isEmpty() ? null : results.get(0);
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find invoices by date
     * @param fecha invoice date
     * @return list of invoices
     */
    public List<Factura> findByFecha(Date fecha) {
        try {
            TypedQuery<Factura> q = em.createNamedQuery("Factura.findByFecha", Factura.class);
            q.setParameter("fecha", fecha);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find invoices by date range
     * @param fechaInicio start date
     * @param fechaFin end date
     * @return list of invoices
     */
    public List<Factura> findByRangoFechas(Date fechaInicio, Date fechaFin) {
        try {
            TypedQuery<Factura> q = em.createNamedQuery("Factura.findByRangoFechas", Factura.class);
            q.setParameter("fechaInicio", fechaInicio);
            q.setParameter("fechaFin", fechaFin);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find invoices by client (socio)
     * @param socio client
     * @return list of invoices
     */
    public List<Factura> findBySocio(Socio socio) {
        try {
            TypedQuery<Factura> q = em.createNamedQuery("Factura.findBySocio", Factura.class);
            q.setParameter("socio", socio);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
    
    /**
     * Find invoices by status
     * @param estado invoice status
     * @return list of invoices
     */
    public List<Factura> findByEstado(String estado) {
        try {
            TypedQuery<Factura> q = em.createNamedQuery("Factura.findByEstado", Factura.class);
            q.setParameter("estado", estado);
            return q.getResultList();
        } catch (Exception e) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
            return null;
        }
    }
}
