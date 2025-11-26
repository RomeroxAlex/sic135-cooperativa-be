package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleEstadoFinanciero;

/**
 * Bean for DetalleEstadoFinanciero entity operations.
 */
@Stateless
@LocalBean
public class DetalleEstadoFinancieroBean extends AbstractDataPersist<DetalleEstadoFinanciero> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public DetalleEstadoFinancieroBean() {
        super(DetalleEstadoFinanciero.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
