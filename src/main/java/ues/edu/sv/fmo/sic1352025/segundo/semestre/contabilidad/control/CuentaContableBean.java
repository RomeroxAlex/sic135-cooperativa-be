/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import java.io.Serializable;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.List;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable;

/**
 *
 * @author jordi
 */
@Stateless
@LocalBean
public class CuentaContableBean extends AbstractDataPersist<CuentaContable> implements Serializable {

      @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public CuentaContableBean(){
        super(CuentaContable.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }
    /**
     * NAMED QUERIE
     * @param nombre
     * @return Lista de cuentas asociadas a una operacion bancaria
     */
      public List<CuentaContable> findByNameOperacionBancaria(final String nombre) {
        try {
            TypedQuery<CuentaContable> q = em.createNamedQuery("CuentaContable.findByNameOperacionBancaria", CuentaContable.class);
            q.setParameter("nombre", nombre);
            System.out.println("Lista de cuentas Encontrada con éxito");
            return q.getResultList();
        }catch (Exception e) {
           Logger.getLogger(getClass().getName()).log(Level.SEVERE, e.getMessage(), e);
        }
        return null;
    }

}
