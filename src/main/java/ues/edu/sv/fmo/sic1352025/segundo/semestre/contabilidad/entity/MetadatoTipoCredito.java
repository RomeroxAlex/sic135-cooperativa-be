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
@Table(name = "metadato_tipo_credito")
@NamedQueries({
    @NamedQuery(name = "MetadatoTipoCredito.findAll", query = "SELECT m FROM MetadatoTipoCredito m"),
    @NamedQuery(name = "MetadatoTipoCredito.findByNombre", query = "SELECT m FROM MetadatoTipoCredito m WHERE m.nombre = :nombre"),
    @NamedQuery(name = "MetadatoTipoCredito.findByRegex", query = "SELECT m FROM MetadatoTipoCredito m WHERE m.regex = :regex")})
public class MetadatoTipoCredito implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Lob
    @Column(name = "id_metadato_tipo_credito")
    private Object idMetadatoTipoCredito;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "regex")
    private String regex;
    @OneToMany(mappedBy = "idMetadatoTipoCredito")
    private Collection<TipoCreditoCaracteristica> tipoCreditoCaracteristicaCollection;

    public MetadatoTipoCredito() {
    }

    public MetadatoTipoCredito(Object idMetadatoTipoCredito) {
        this.idMetadatoTipoCredito = idMetadatoTipoCredito;
    }

    public Object getIdMetadatoTipoCredito() {
        return idMetadatoTipoCredito;
    }

    public void setIdMetadatoTipoCredito(Object idMetadatoTipoCredito) {
        this.idMetadatoTipoCredito = idMetadatoTipoCredito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRegex() {
        return regex;
    }

    public void setRegex(String regex) {
        this.regex = regex;
    }

    public Collection<TipoCreditoCaracteristica> getTipoCreditoCaracteristicaCollection() {
        return tipoCreditoCaracteristicaCollection;
    }

    public void setTipoCreditoCaracteristicaCollection(Collection<TipoCreditoCaracteristica> tipoCreditoCaracteristicaCollection) {
        this.tipoCreditoCaracteristicaCollection = tipoCreditoCaracteristicaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMetadatoTipoCredito != null ? idMetadatoTipoCredito.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MetadatoTipoCredito)) {
            return false;
        }
        MetadatoTipoCredito other = (MetadatoTipoCredito) object;
        if ((this.idMetadatoTipoCredito == null && other.idMetadatoTipoCredito != null) || (this.idMetadatoTipoCredito != null && !this.idMetadatoTipoCredito.equals(other.idMetadatoTipoCredito))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ues.edu.sv.fmo.sic1352025.segundo.semestre.contabilidad.entity.MetadatoTipoCredito[ idMetadatoTipoCredito=" + idMetadatoTipoCredito + " ]";
    }
    
}
