package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;


import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.AsientoContable;

 @Stateless
@LocalBean
public class AsientoContableBean extends AbstractDataPersist<AsientoContable>{


   
    
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public AsientoContableBean(){
        super(AsientoContable.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }


}
