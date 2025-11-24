/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Collection;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

/**
 *
 * @author jordi
 */
@Entity
@Table(name = "credito")
@NamedQueries({
    @NamedQuery(name = "Credito.findAll", query = "SELECT c FROM Credito c"),
    @NamedQuery(name = "Credito.findByFecha", query = "SELECT c FROM Credito c WHERE c.fecha = :fecha"),
    @NamedQuery(name = "Credito.findByInteres", query = "SELECT c FROM Credito c WHERE c.interes = :interes"),
    @NamedQuery(name = "Credito.findByMonto", query = "SELECT c FROM Credito c WHERE c.monto = :monto"),
    @NamedQuery(name = "Credito.findByDocumentacion", query = "SELECT c FROM Credito c WHERE c.documentacion = :documentacion"),
    @NamedQuery(name = "Credito.findByActivo", query = "SELECT c FROM Credito c WHERE c.activo = :activo")})
public class Credito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_credito")
    private Object idCredito;
    @Column(name = "fecha")
    @Temporal(TemporalType.TIMESTAMP)
    private Date fecha;
    @Column(name = "interes")
    private BigInteger interes;
    @Column(name = "monto")
    private BigInteger monto;
    @Column(name = "documentacion")
    private String documentacion;
    @Column(name = "activo")
    private Boolean activo;
    @OneToMany(mappedBy = "idCredito")
    private Collection<CreditoSocio> creditoSocioCollection;
    @JoinColumn(name = "id_tipo_credito", referencedColumnName = "id_tipo_credito")
    @ManyToOne
    private TipoCredito idTipoCredito;

    public Credito() {
    }

    public Credito(Object idCredito) {
        this.idCredito = idCredito;
    }

    public Object getIdCredito() {
        return idCredito;
    }

    public void setIdCredito(Object idCredito) {
        this.idCredito = idCredito;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public BigInteger getInteres() {
        return interes;
    }

    public void setInteres(BigInteger interes) {
        this.interes = interes;
    }

    public BigInteger getMonto() {
        return monto;
    }

    public void setMonto(BigInteger monto) {
        this.monto = monto;
    }

    public String getDocumentacion() {
        return documentacion;
    }

    public void setDocumentacion(String documentacion) {
        this.documentacion = documentacion;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Collection<CreditoSocio> getCreditoSocioCollection() {
        return creditoSocioCollection;
    }

    public void setCreditoSocioCollection(Collection<CreditoSocio> creditoSocioCollection) {
        this.creditoSocioCollection = creditoSocioCollection;
    }

    public TipoCredito getIdTipoCredito() {
        return idTipoCredito;
    }

    public void setIdTipoCredito(TipoCredito idTipoCredito) {
        this.idTipoCredito = idTipoCredito;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idCredito != null ? idCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Credito)) {
            return false;
        }
        Credito other = (Credito) object;
        if ((this.idCredito == null && other.idCredito != null) || (this.idCredito != null && !this.idCredito.equals(other.idCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.Credito[ idCredito=" + idCredito + " ]";
    }
    
}
