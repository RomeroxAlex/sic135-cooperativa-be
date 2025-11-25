package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaOperacion;
import java.util.logging.Logger;
import java.util.logging.Level;

@Stateless
@LocalBean
public class CuentaOperacionBean extends AbstractDataPersist<CuentaOperacion> implements Serializable{
      
  @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public CuentaOperacionBean(){
        super(CuentaOperacion.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /**
     * NAMED QUERIE NATURALEZA
     */
        public String findNaturalezaCuentaOperacion(final String nombreOperacion, final CuentaContable cuentaContable) {
        try {
            TypedQuery<String> q = em.createNamedQuery("CuentaOperacion.findNaturalezaCuentaOperacion", String.class);
            q.setParameter("nombre", nombreOperacion);
            q.setParameter("cuentaContable", cuentaContable);
            q.setMaxResults(1);
            return q.getSingleResult();
        }catch (Exception e) {
           Logger.getLogger(getClass().getName()).log(Level.SEVERE,e.getMessage(),e);
        }
        return null;
    }


}
