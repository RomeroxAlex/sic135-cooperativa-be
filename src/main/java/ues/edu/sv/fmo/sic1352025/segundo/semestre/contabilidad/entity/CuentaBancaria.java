/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "cuenta_bancaria")
@NamedQueries({
    @NamedQuery(name = "CuentaBancaria.findAll", query = "SELECT c FROM CuentaBancaria c"),
    @NamedQuery(name = "CuentaBancaria.findByActivo", query = "SELECT c FROM CuentaBancaria c WHERE c.activo = :activo"),
    @NamedQuery(name = "CuentaBancaria.findBySaldo", query = "SELECT c FROM CuentaBancaria c WHERE c.saldo = :saldo")})
public class CuentaBancaria implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cuenta_bancaria")
    private UUID idCuentaBancaria;
    @Column(name = "activo")
    private Boolean activo;
    @Column(name = "saldo")
    private BigInteger saldo;
    @JoinColumn(name = "id_socio", referencedColumnName = "id_socio")
    @ManyToOne
    private Socio idSocio;
    @JoinColumn(name = "id_tipo_cuenta_bancaria", referencedColumnName = "id_tipo_cuenta_bancaria")
    @ManyToOne
    private TipoCuentaBancaria idTipoCuentaBancaria;
    @OneToMany(mappedBy = "idCuentaBancaria")
    private Collection<TarjetaBancaria> tarjetaBancariaCollection;

    public CuentaBancaria() {
    }

    public CuentaBancaria(UUID idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public UUID getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public void setIdCuentaBancaria(UUID idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public BigInteger getSaldo() {
        return saldo;
    }

    public void setSaldo(BigInteger saldo) {
        this.saldo = saldo;
    }

    public Socio getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Socio idSocio) {
        this.idSocio = idSocio;
    }

    public TipoCuentaBancaria getIdTipoCuentaBancaria() {
        return idTipoCuentaBancaria;
    }

    public void setIdTipoCuentaBancaria(TipoCuentaBancaria idTipoCuentaBancaria) {
        this.idTipoCuentaBancaria = idTipoCuentaBancaria;
    }

    public Collection<TarjetaBancaria> getTarjetaBancariaCollection() {
        return tarjetaBancariaCollection;
    }

    public void setTarjetaBancariaCollection(Collection<TarjetaBancaria> tarjetaBancariaCollection) {
        this.tarjetaBancariaCollection = tarjetaBancariaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuentaBancaria != null ? idCuentaBancaria.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CuentaBancaria)) {
            return false;
        }
        CuentaBancaria other = (CuentaBancaria) object;
        if ((this.idCuentaBancaria == null && other.idCuentaBancaria != null) || (this.idCuentaBancaria != null && !this.idCuentaBancaria.equals(other.idCuentaBancaria))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaBancaria[ idCuentaBancaria=" + idCuentaBancaria + " ]";
    }
    
}
