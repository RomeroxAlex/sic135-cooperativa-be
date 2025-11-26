package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.OperacionBancaria;

@Stateless
@LocalBean
public class OperacionBancariaBean extends AbstractDataPersist<OperacionBancaria> implements Serializable{

      
  @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public OperacionBancariaBean(){
        super(OperacionBancaria.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

    /*
        NAMED QUERY
     */
    public OperacionBancaria findByName(final String nombre) {
        try {
            TypedQuery<OperacionBancaria> q = em.createNamedQuery("OperacionBancaria.findByNombre", OperacionBancaria.class);
            q.setParameter("nombre", nombre);
            q.setMaxResults(1);
            return q.getSingleResult();
        }catch (Exception e) {
           Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }


}
