/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.control;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import java.io.Serializable;

import jakarta.persistence.PersistenceContext;
import ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaBancaria;

/**
 *
 * @author jordi
 */
@Stateless
@LocalBean
public class CuentaBancariaBean extends AbstractDataPersist<CuentaBancaria> implements Serializable{
    @PersistenceContext(unitName = "ContabilidadPU")
    EntityManager em;
    
    public CuentaBancariaBean(){
        super(CuentaBancaria.class);
    }

    public EntityManager getEntityManager() {
        return em;
    }

   
}
