package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import java.io.Serializable;

import jakarta.persistence.PersistenceContext;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.MetadatoTipoCredito;

@Stateless
@LocalBean
public class MetadatoTipoCreditoBean extends AbstractDataPersist<MetadatoTipoCredito> implements Serializable {
  
  @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public MetadatoTipoCreditoBean(){
        super(MetadatoTipoCredito.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }
}
