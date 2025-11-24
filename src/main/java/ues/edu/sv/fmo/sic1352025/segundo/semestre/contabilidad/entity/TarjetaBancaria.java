/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "tarjeta_bancaria")
@NamedQueries({
    @NamedQuery(name = "TarjetaBancaria.findAll", query = "SELECT t FROM TarjetaBancaria t"),
    @NamedQuery(name = "TarjetaBancaria.findBySaldo", query = "SELECT t FROM TarjetaBancaria t WHERE t.saldo = :saldo"),
    @NamedQuery(name = "TarjetaBancaria.findByFechaVencimiento", query = "SELECT t FROM TarjetaBancaria t WHERE t.fechaVencimiento = :fechaVencimiento"),
    @NamedQuery(name = "TarjetaBancaria.findByDocumentacion", query = "SELECT t FROM TarjetaBancaria t WHERE t.documentacion = :documentacion")})
public class TarjetaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_tarjeta_bancaria")
    private Object idTarjetaBancaria;
    @Column(name = "saldo")
    private BigInteger saldo;
    @Column(name = "fecha_vencimiento")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fechaVencimiento;
    @Column(name = "documentacion")
    private String documentacion;
    @JoinColumn(name = "id_cuenta_bancaria", referencedColumnName = "id_cuenta_bancaria")
    @ManyToOne
    private CuentaBancaria idCuentaBancaria;
    @JoinColumn(name = "id_tipo_tarjeta_bancaria", referencedColumnName = "id_tipo_tarjeta_bancaria")
    @ManyToOne
    private TipoTarjetaBancaria idTipoTarjetaBancaria;

    public TarjetaBancaria() {
    }

    public TarjetaBancaria(Object idTarjetaBancaria) {
        this.idTarjetaBancaria = idTarjetaBancaria;
    }

    public Object getIdTarjetaBancaria() {
        return idTarjetaBancaria;
    }

    public void setIdTarjetaBancaria(Object idTarjetaBancaria) {
        this.idTarjetaBancaria = idTarjetaBancaria;
    }

    public BigInteger getSaldo() {
        return saldo;
    }

    public void setSaldo(BigInteger saldo) {
        this.saldo = saldo;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public String getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(String documentacion) {
        this.documentacion = documentacion;
    }

    public CuentaBancaria getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public void setIdCuentaBancaria(CuentaBancaria idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public TipoTarjetaBancaria getIdTipoTarjetaBancaria() {
        return idTipoTarjetaBancaria;
    }

    public void setIdTipoTarjetaBancaria(TipoTarjetaBancaria idTipoTarjetaBancaria) {
        this.idTipoTarjetaBancaria = idTipoTarjetaBancaria;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTarjetaBancaria != null ? idTarjetaBancaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TarjetaBancaria)) {
            return false;
        }
        TarjetaBancaria other = (TarjetaBancaria) object;
        if ((this.idTarjetaBancaria == null && other.idTarjetaBancaria != null) || (this.idTarjetaBancaria != null && !this.idTarjetaBancaria.equals(other.idTarjetaBancaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.TarjetaBancaria[ idTarjetaBancaria=" + idTarjetaBancaria + " ]";
    }
    
}
