/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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
@Table(name = "cuenta_contable")
@NamedQueries({
    @NamedQuery(name = "CuentaContable.findAll", query = "SELECT c FROM CuentaContable c"),
    @NamedQuery(name = "CuentaContable.findByNombre", query = "SELECT c FROM CuentaContable c WHERE c.nombre = :nombre"),
    @NamedQuery(name = "CuentaContable.findByCodigo", query = "SELECT c FROM CuentaContable c WHERE c.codigo = :codigo"),
    @NamedQuery(name = "CuentaContable.findByNameOperacionBancaria", query = "SELECT co.idCuentaContable FROM CuentaOperacion co WHERE co.idOperacionBancaria.nombre = :nombre")

})
public class CuentaContable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id_cuenta_contable", columnDefinition = "uuid")
    private UUID idCuentaContable;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "codigo")
    private BigInteger codigo;
       
    @JoinColumn(name = "id_tipo_cuenta_contable", referencedColumnName = "id_tipo_cuenta_contable", columnDefinition = "uuid")
    @ManyToOne(fetch = FetchType.LAZY )
   // @JsonbTransient
    private TipoCuentaContable idTipoCuentaContable;

    @OneToMany(mappedBy = "idCuentaContable")
    @JsonbTransient
    private List<CuentaOperacion> cuentaOperacionCollection;

    @Column(name = "naturaleza")
    private String naturaleza;

    public CuentaContable() {
    }

    public CuentaContable(UUID idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public UUID getIdCuentaContable() {
        return idCuentaContable;
    }

    public void setIdCuentaContable(UUID idCuentaContable) {
        this.idCuentaContable = idCuentaContable;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigInteger getCodigo() {
        return codigo;
    }

    public void setCodigo(BigInteger codigo) {
        this.codigo = codigo;
    }

    
    @JsonbTransient
    public TipoCuentaContable getIdTipoCuentaContable() {
        return idTipoCuentaContable;
    }

    public void setIdTipoCuentaContable(TipoCuentaContable idTipoCuentaContable) {
        this.idTipoCuentaContable = idTipoCuentaContable;
    }
    
    
    public List<CuentaOperacion> getCuentaOperacionCollection() {
        return cuentaOperacionCollection;
    }

    public void setCuentaOperacionCollection(List<CuentaOperacion> cuentaOperacionCollection) {
        this.cuentaOperacionCollection = cuentaOperacionCollection;
    }


    public String getNaturaleza() {
        return naturaleza;
    }

    public void setNaturaleza(String naturaleza) {
        this.naturaleza = naturaleza;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCuentaContable != null ? idCuentaContable.hashCode() : 0);
        return hash;
    }

   

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CuentaContable[ idCuentaContable=" + idCuentaContable + " ]";
    }
    
}
