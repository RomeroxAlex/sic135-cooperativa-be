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
@Table(name = "cabecera_transaccion")
@NamedQueries({
    @NamedQuery(name = "CabeceraTransaccion.findAll", query = "SELECT c FROM CabeceraTransaccion c"),
    @NamedQuery(name = "CabeceraTransaccion.findByFecha", query = "SELECT c FROM CabeceraTransaccion c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "CabeceraTransaccion.findByTablaOrigen", query = "SELECT c FROM CabeceraTransaccion c WHERE c.tablaOrigen = :tablaOrigen"),
    @NamedQuery(name = "CabeceraTransaccion.findByObservaciones", query = "SELECT c FROM CabeceraTransaccion c WHERE c.observaciones = :observaciones"),
    @NamedQuery(name = "CabeceraTransaccion.findByMonto", query = "SELECT c FROM CabeceraTransaccion c WHERE c.monto = :monto"),
    @NamedQuery(name = "CabeceraTransaccion.findByMoneda", query = "SELECT c FROM CabeceraTransaccion c WHERE c.moneda = :moneda")})
public class CabeceraTransaccion implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_cabecera_transaccion")
    private Object idCabeceraTransaccion;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "tabla_origen")
    private String tablaOrigen;
    @Lob
    @Column(name = "id_origen")
    private Object idOrigen;
    @Column(name = "observaciones")
    private String observaciones;
    @Lob
    @Column(name = "id_socio")
    private Object idSocio;
    @Column(name = "monto")
    private BigInteger monto;
    @Column(name = "moneda")
    private String moneda;
    @JoinColumn(name = "id_operacion_bancaria", referencedColumnName = "id_operacion_bancaria")
    @ManyToOne
    private OperacionBancaria idOperacionBancaria;
    @JoinColumn(name = "id_transaccion", referencedColumnName = "id_transaccion")
    @ManyToOne
    private Transaccion idTransaccion;

    public CabeceraTransaccion() {
    }

    public CabeceraTransaccion(Object idCabeceraTransaccion) {
        this.idCabeceraTransaccion = idCabeceraTransaccion;
    }

    public Object getIdCabeceraTransaccion() {
        return idCabeceraTransaccion;
    }

    public void setIdCabeceraTransaccion(Object idCabeceraTransaccion) {
        this.idCabeceraTransaccion = idCabeceraTransaccion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getTablaOrigen() {
        return tablaOrigen;
    }

    public void setTablaOrigen(String tablaOrigen) {
        this.tablaOrigen = tablaOrigen;
    }

    public Object getIdOrigen() {
        return idOrigen;
    }

    public void setIdOrigen(Object idOrigen) {
        this.idOrigen = idOrigen;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Object getIdSocio() {
        return idSocio;
    }

    public void setIdSocio(Object idSocio) {
        this.idSocio = idSocio;
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public String getMoneda() {
        return moneda;
    }

    public void setMoneda(String moneda) {
        this.moneda = moneda;
    }

    public OperacionBancaria getIdOperacionBancaria() {
        return idOperacionBancaria;
    }

    public void setIdOperacionBancaria(OperacionBancaria idOperacionBancaria) {
        this.idOperacionBancaria = idOperacionBancaria;
    }

    public Transaccion getIdTransaccion() {
        return idTransaccion;
    }

    public void setIdTransaccion(Transaccion idTransaccion) {
        this.idTransaccion = idTransaccion;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCabeceraTransaccion != null ? idCabeceraTransaccion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof CabeceraTransaccion)) {
            return false;
        }
        CabeceraTransaccion other = (CabeceraTransaccion) object;
        if ((this.idCabeceraTransaccion == null && other.idCabeceraTransaccion != null) || (this.idCabeceraTransaccion != null && !this.idCabeceraTransaccion.equals(other.idCabeceraTransaccion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.CabeceraTransaccion[ idCabeceraTransaccion=" + idCabeceraTransaccion + " ]";
    }
    
}
