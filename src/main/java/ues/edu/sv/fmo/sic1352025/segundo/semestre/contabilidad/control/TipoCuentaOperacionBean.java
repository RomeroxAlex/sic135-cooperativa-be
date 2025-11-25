package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;


import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCuentaOperacion;

@Stateless
@LocalBean
public class TipoCuentaOperacionBean extends AbstractDataPersist<TipoCuentaOperacion>{


          
  @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public TipoCuentaOperacionBean(){
        super(TipoCuentaOperacion.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }


}

