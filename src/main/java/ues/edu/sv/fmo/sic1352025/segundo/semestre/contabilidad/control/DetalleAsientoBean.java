package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;


import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.DetalleAsiento;

@Stateless
@LocalBean
public class DetalleAsientoBean extends AbstractDataPersist<DetalleAsiento>{

   
          
  @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public DetalleAsientoBean(){
        super(DetalleAsiento.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }



}
