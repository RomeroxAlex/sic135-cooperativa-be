/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "cuenta_operacion")
@NamedQueries({
    @NamedQuery(name = "CuentaOperacion.findAll", query = "SELECT c FROM CuentaOperacion c")})
public class CuentaOperacion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_cuenta_operacion")
    private Object idCuentaOperacion;
    @JoinColumn(name = "id_cuenta_contable", referencedColumnName = "id_cuenta_contable")
    @ManyToOne
    private CuentaContable idCuentaContable;
    @JoinColumn(name = "id_operacion_bancaria", referencedColumnName = "id_operacion_bancaria")
    @ManyToOne
    private OperacionBancaria idOperacionBancaria;

    public CuentaOperacion() {
    }

    public CuentaOperacion(Object idCuentaOperacion) {
        this.idCuentaOperacion = idCuentaOperacion;
    }

    public Object getIdCuentaOperacion() {
        return idCuentaOperacion;
    }

    public void setIdCuentaOperacion(Object idCuentaOperacion) {
        this.idCuentaOperacion = idCuentaOperacion;
    }

    public CuentaContable getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(CuentaContable idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public OperacionBancaria getIdOperacionBancaria() {
        return idOperacionBancaria;
    }

    public void setIdOperacionBancaria(OperacionBancaria idOperacionBancaria) {
        this.idOperacionBancaria = idOperacionBancaria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuentaOperacion != null ? idCuentaOperacion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaOperacion)) {
            return false;
        }
        CuentaOperacion other = (CuentaOperacion) object;
        if ((this.idCuentaOperacion == null && other.idCuentaOperacion != null) || (this.idCuentaOperacion != null && !this.idCuentaOperacion.equals(other.idCuentaOperacion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaOperacion[ idCuentaOperacion=" + idCuentaOperacion + " ]";
    }
    
}
