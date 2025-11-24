/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.util.Collection;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "tipo_cuenta_bancaria")
@NamedQueries({
    @NamedQuery(name = "TipoCuentaBancaria.findAll", query = "SELECT t FROM TipoCuentaBancaria t"),
    @NamedQuery(name = "TipoCuentaBancaria.findByNombre", query = "SELECT t FROM TipoCuentaBancaria t WHERE t.nombre = :nombre")})
public class TipoCuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_tipo_cuenta_bancaria")
    private Object idTipoCuentaBancaria;
    @Column(name = "nombre")
    private String nombre;
    @OneToMany(mappedBy = "idTipoCuentaBancaria")
    private Collection<CuentaBancaria> cuentaBancariaCollection;

    public TipoCuentaBancaria() {
    }

    public TipoCuentaBancaria(Object idTipoCuentaBancaria) {
        this.idTipoCuentaBancaria = idTipoCuentaBancaria;
    }

    public Object getIdTipoCuentaBancaria() {
        return idTipoCuentaBancaria;
    }

    public void setIdTipoCuentaBancaria(Object idTipoCuentaBancaria) {
        this.idTipoCuentaBancaria = idTipoCuentaBancaria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Collection<CuentaBancaria> getCuentaBancariaCollection() {
        return cuentaBancariaCollection;
    }

    public void setCuentaBancariaCollection(Collection<CuentaBancaria> cuentaBancariaCollection) {
        this.cuentaBancariaCollection = cuentaBancariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoCuentaBancaria != null ? idTipoCuentaBancaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoCuentaBancaria)) {
            return false;
        }
        TipoCuentaBancaria other = (TipoCuentaBancaria) object;
        if ((this.idTipoCuentaBancaria == null && other.idTipoCuentaBancaria != null) || (this.idTipoCuentaBancaria != null && !this.idTipoCuentaBancaria.equals(other.idTipoCuentaBancaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TipoCuentaBancaria[ idTipoCuentaBancaria=" + idTipoCuentaBancaria + " ]";
    }
    
}
