/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "tipo_cuenta_contable")
@NamedQueries({
    @NamedQuery(name = "TipoCuentaContable.findAll", query = "SELECT t FROM TipoCuentaContable t"),
    @NamedQuery(name = "TipoCuentaContable.findByNombre", query = "SELECT t FROM TipoCuentaContable t WHERE t.nombre = :nombre")})
public class TipoCuentaContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_tipo_cuenta_contable")
    private UUID idTipoCuentaContable;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idTipoCuentaContable")
    private Collection<CuentaContable> cuentaContableCollection;

    public TipoCuentaContable() {
    }

    public TipoCuentaContable(UUID idTipoCuentaContable) {
        this.idTipoCuentaContable = idTipoCuentaContable;
    }

    public UUID getIdTipoCuentaContable() {
        return idTipoCuentaContable;
    }

    public void setIdTipoCuentaContable(UUID idTipoCuentaContable) {
        this.idTipoCuentaContable = idTipoCuentaContable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<CuentaContable> getCuentaContableCollection() {
        return cuentaContableCollection;
    }

    public void setCuentaContableCollection(Collection<CuentaContable> cuentaContableCollection) {
        this.cuentaContableCollection = cuentaContableCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCuentaContable != null ? idTipoCuentaContable.hashCode() : 0);
        return hash;
    }


    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCuentaContable[ idTipoCuentaContable=" + idTipoCuentaContable + " ]";
    }
    
}
