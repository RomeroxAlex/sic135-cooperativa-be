package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleFactura;

/**
 * Bean for DetalleFactura entity operations.
 */
@Stateless
@LocalBean
public class DetalleFacturaBean extends AbstractDataPersist<DetalleFactura> implements Serializable {
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public DetalleFacturaBean() {
        super(DetalleFactura.class);
    }
    
    @Override
    public EntityManager getEntityManager() {
        return em;
    }
}
